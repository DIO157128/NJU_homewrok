import copy
import logging
import random

import torch
from torch import nn

from methods.finetune import Finetune
import torch.nn.functional as F

logger = logging.getLogger()
class iCaRL(Finetune):
    def __init__(self, criterion, device, train_transform, test_transform, task_classes, n_classes, temperature, memory_budget, **kwargs):
        super(iCaRL, self).__init__(criterion, device, train_transform, test_transform, task_classes, n_classes, **kwargs)
        self.temperature = temperature
        self.exemplar_sets = {}
        self.old_model = None
        self.classes = []  # 类别列表初始化为空列表
        self.memory_budget = memory_budget  # 设置 memory_budget 的初始值

    def before_task(self, cur_train_datalist):
        super(iCaRL, self).before_task(cur_train_datalist)
        self.old_model = copy.deepcopy(self.model)
        self.old_model = self.old_model.to(self.device)
        self.old_model.eval()

        # 更新类别列表
        self.classes = list(set([data['label'] for data in cur_train_datalist]))



    def exemplar_selection(self, cur_train_datalist, class_list, m):
        class_indices = []
        exemplar_set = []

        # 获取当前任务的类别索引
        for class_name in class_list:
            class_idx = self.classes.index(class_name)
            class_indices.append(class_idx)

        # 遍历当前任务的类别
        for class_idx in class_indices:
            # 根据类别索引获取当前类别的样本列表
            samples = [data for data in cur_train_datalist if data['label'] == class_idx]
            random.shuffle(samples)

            # 选择前 m 个样本作为示例样本
            exemplar_set.extend(samples[:m])

        return exemplar_set

    def reduce_exemplar_set(self, m):
        for class_idx in self.exemplar_sets.keys():
            self.exemplar_sets[class_idx] = self.exemplar_sets[class_idx][:m]

    def train(self, cur_iter):
        # 获取当前任务的类别列表
        class_list = self.classes[:cur_iter+1]

        # 为当前任务选择示例样本
        exemplar_set = self.exemplar_selection(self.train_list, class_list, self.memory_budget)
        self.exemplar_sets[cur_iter] = exemplar_set

        # 将示例样本添加到训练集中
        train_dataset = self.train_list + exemplar_set
        self.set_train_dataset(train_dataset)

        #训练代码
        logger.info("#" * 10 + "Start Training" + "#" * 10)
        train_list = self.train_list + self.memory_list
        test_list = self.test_list
        train_loader, test_loader = self.get_dataloader(
            self.batch_size, self.n_woker, train_list, test_list
        )

        logger.info(f"New training samples: {len(self.train_list)}")
        logger.info(f"In-memory samples: {len(self.memory_list)}")
        logger.info(f"Train samples: {len(train_list)}")
        logger.info(f"Test samples: {len(test_list)}")

        best_acc = 0.0
        eval_dict = dict()
        n_batches = len(train_loader)
        best_loss = 1000.0
        patience = 0
        for epoch in range(self.n_epoch):
            if epoch > 0:
                self.scheduler.step()

            total_loss, correct, num_data = 0.0, 0.0, 0.0
            self.model.train()
            for i, data in enumerate(train_loader):
                x = data['image'].to(self.device)
                y = data['label'].to(self.device)

                self.optimizer.zero_grad()

                logit = self.model(x)
                loss = self.criterion(logit, y)
                preds = torch.argmax(logit, dim=-1)
                loss.backward()
                self.optimizer.step()
                total_loss += loss.item()
                correct += torch.sum(preds == y).item()
                num_data += y.size(0)

            eval_dict = self.evaluation(test_loader=test_loader, criterion=self.criterion)

            cls_acc = "cls_acc: ["
            for _ in eval_dict['cls_acc']:
                cls_acc += format(_, '.3f') + ', '
            cls_acc += ']'

            logger.info(
                f"Task {cur_iter} | Epoch {epoch + 1}/{self.n_epoch} | lr {self.optimizer.param_groups[0]['lr']:.4f} | train_loss {total_loss / n_batches:.4f} | train_acc {correct / num_data:.4f} | "
                f"test_loss {eval_dict['avg_loss']:.4f} | test_acc {eval_dict['avg_acc']:.4f} |"
            )

            # 输出每个类别的准确率，但是cifar100类别数目一多，输出就太乱了，取决你们自己
            # logger.info(cls_acc)

            best_acc = max(best_acc, eval_dict["avg_acc"])
            if eval_dict['avg_loss']<best_loss:
                best_loss = eval_dict['avg_loss']
                patience = 0
            else:
                patience+=1
            if patience>=5:
                break

        # 减少示例样本集的大小
        self.reduce_exemplar_set(self.memory_budget)

        return best_acc, eval_dict

    def compute_class_means(self):
        class_means = {}

        # 遍历每个类别的示例样本集合
        for class_idx, exemplar_set in self.exemplar_sets.items():
            exemplar_features = []
            for exemplar in exemplar_set:
                with torch.no_grad():
                    inputs = self.test_transform(exemplar['file_name']).unsqueeze(0).to(self.device)
                    features = self.model.extract_features(inputs)
                exemplar_features.append(features.squeeze(0))

            exemplar_features = torch.stack(exemplar_features)
            class_mean = exemplar_features.mean(dim=0)
            class_means[class_idx] = class_mean

        return class_means

    def classify(self, inputs):
        class_means = self.compute_class_means()
        inputs = inputs.to(self.device)
        features = self.model.extract_features(inputs)
        scores = []

        for class_idx in range(self.n_classes):
            if class_idx in class_means:
                class_mean = class_means[class_idx].unsqueeze(0)
                class_mean = class_mean.repeat(features.size(0), 1)
                dist = F.cosine_similarity(features, class_mean, dim=1)
                scores.append(dist)
            else:
                scores.append(torch.zeros(features.size(0)).to(self.device))

        scores = torch.stack(scores, dim=1)
        return scores

    def update_representation(self):
        if self.old_model is not None:
            self.model.fc = nn.Linear(self.model.fc.in_features, self.n_classes)
            self.model.fc.weight.data[:self.old_model.fc.weight.size(0)] = self.old_model.fc.weight.data.clone()
            self.model.fc.bias.data[:self.old_model.fc.bias.size(0)] = self.old_model.fc.bias.data.clone()

    def after_task(self, cur_iter):
        super(iCaRL, self).after_task(cur_iter)
        self.old_model = None
        self.exemplar_sets = {}

    def set_train_dataset(self, train_dataset):
        self.train_list = train_dataset
        self.memory_budget = len(train_dataset)