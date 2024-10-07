from copy import deepcopy

import torch
from torch import nn
import torch.nn.functional as F

from methods.finetune import Finetune


class LwF(Finetune):
    def __init__(self, criterion, device, train_transform, test_transform, init_class, n_classes, **kwargs):
        super().__init__(criterion, device, train_transform, test_transform, init_class, n_classes, **kwargs)
        self.prev_model = None

    def before_task(self, datalist):
        super().before_task(datalist)
        self.prev_model = deepcopy(self.model)

    def after_task(self, cur_iter):
        self.num_learned_class = self.num_learning_class
        # update memory list if needed
        # random sample

        k = self.memory_size // self.num_learning_class
        tmp = [[] for _ in range(self.num_learning_class)]
        for _ in self.memory_list + self.train_list:
            tmp[_['label']].append(_)
        self.memory_list = []
        for _ in tmp:
            self.memory_list.extend(_[:k])

        # Regularization loss
        reg_loss = 0.0
        if self.prev_model is not None:
            self.prev_model = self.prev_model.to(self.device)
            self.prev_model.eval()

            # Freeze the parameters of the previous model
            for param in self.prev_model.parameters():
                param.requires_grad = False

            # Compute the regularization loss
            with torch.no_grad():
                for i, data in enumerate(self.train_loader):
                    x = data['image'].to(self.device)
                    logit_prev = self.prev_model(x)
                    logit_cur = self.model(x)
                    reg_loss += self.compute_kd_loss(logit_cur, logit_prev)

        # Classification loss
        classification_loss = 0.0
        for i, data in enumerate(self.train_loader):
            x = data['image'].to(self.device)
            y = data['label'].to(self.device)
            logits = self.model(x)
            classification_loss += self.criterion(logits, y)

        # Total loss = Classification loss + Regularization loss
        total_loss = classification_loss + reg_loss

        # Backpropagation and optimization
        self.optimizer.zero_grad()
        total_loss.backward()
        self.optimizer.step()

        # Update the previous model
        self.prev_model = deepcopy(self.model)

    def compute_kd_loss(self, logit_cur, logit_prev):
        alpha = 0.1  # Temperature parameter for knowledge distillation
        kd_loss = nn.KLDivLoss()(F.log_softmax(logit_cur / alpha, dim=1), F.softmax(logit_prev / alpha, dim=1))
        return kd_loss
