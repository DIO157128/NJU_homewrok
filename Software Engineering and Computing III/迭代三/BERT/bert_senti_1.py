import argparse
import os

import pandas as pd
import torch
import time
import numpy as np
import torch.nn as nn
from transformers import AdamW, get_linear_schedule_with_warmup
from transformers import RobertaTokenizer, RobertaModel
from torch.utils.data import TensorDataset, DataLoader, RandomSampler, SequentialSampler
import dataset_load
import preprocess.process_text as preprocess
import results_persist as Accuracy
from transformers import AutoTokenizer, AutoModelForSequenceClassification
from transformers import AutoModel
import torch.nn.functional as F




# 进行token,预处理
def preprocessing_for_bert(data, tokenizer, MAX_LEN):
    # 空列表来储存信息
    input_ids = []
    attention_masks = []

    # 每个句子循环一次
    for sent in data:
        encoded_sent = tokenizer.encode_plus(
            text=sent,  # 预处理语句
            add_special_tokens=True,  # 加 [CLS] 和 [SEP]
            max_length=MAX_LEN,  # 截断或者填充的最大长度
            padding='max_length',  # 填充为最大长度，这里的padding在之间可以直接用pad_to_max但是版本更新之后弃用了，老版本什么都没有，可以尝试用extend方法
            return_attention_mask=True  # 返回 attention mask
        )

        # 把输出加到列表里面
        input_ids.append(encoded_sent.get('input_ids'))
        attention_masks.append(encoded_sent.get('attention_mask'))

    # 把list转换为tensor
    input_ids = torch.tensor(input_ids)
    attention_masks = torch.tensor(attention_masks)

    return input_ids, attention_masks


# Define a residual block class
class ResidualBlock(nn.Module):
    def __init__(self, in_channels, out_channels, kernel_size, padding):
        super(ResidualBlock, self).__init__()
        # Define two convolutional layers with padding
        self.conv1 = nn.Conv1d(in_channels, out_channels, kernel_size, padding=padding)
        self.conv2 = nn.Conv1d(out_channels, out_channels, kernel_size, padding=padding)
        # Define a skip connection
        self.skip = nn.Conv1d(in_channels, out_channels, 1) if in_channels != out_channels else nn.Identity()

    def forward(self, x):
        # Apply the first convolutional layer
        out = F.relu(self.conv1(x))
        # Apply the second convolutional layer
        out = F.relu(self.conv2(out))
        # Add the input and the output
        out = self.skip(x) + out
        return out


# 自己定义的Bert分类器的类，微调Bert模型
class BertClassifier(nn.Module):
    def __init__(self, freeze_bert=0):
        """
        freeze_bert (bool): 设置是否进行微调，0就是不，1就是调
        """
        super(BertClassifier, self).__init__()
        # 输入维度(hidden size of Bert)默认768，分类器隐藏维度，输出维度(label)
        D_in, H, D_out = 768, 100, 3

         # 实体化Bert模型
        # self.bert = AutoModel.from_pretrained("microsoft/deberta-base")
        self.bert = RobertaModel.from_pretrained("cardiffnlp/twitter-roberta-base-sentiment")

        # Add a MultiheadAttention layer
        self.attention = nn.MultiheadAttention(D_in, num_heads=4)


        # Add padding to the CNN layer
        self.cnn = nn.Conv1d(D_in, H, kernel_size=3, padding=1)

        # Add padding to the residual block
        self.res_block = ResidualBlock(H, H, kernel_size=3, padding=1)

        # Add a Bidirectional GRU layer
        self.gru = nn.GRU(H, H // 2, batch_first=True, bidirectional=True) # 添加一个双向GRU层

        self.lstm = nn.LSTM(H, H // 2, batch_first=True)

        # Add an Adaptive Max Pooling layer
        self.adaptive_max_pool = nn.AdaptiveMaxPool1d(1) # 添加一个自适应最大池化层
        
        

        # 实体化一个单层前馈分类器，说白了就是最后要输出的时候搞个全连接层
        self.classifier = nn.Sequential(
            nn.Linear(H//2, H // 4),  # 全连接
            nn.BatchNorm1d(H // 4),
            nn.ReLU(),  # 激活函数
            nn.Dropout(0.2),  # Dropout
            nn.Linear(H // 4, 50),  # 全连接
            nn.ReLU(),
            nn.Dropout(0.2),
            nn.Linear(50, D_out),
            nn.Softmax(dim=1)
        )
    
    
    
    
    
    

    def forward(self, input_ids, attention_mask):
        # 开始搭建整个网络了
        # 输入
        outputs = self.bert(input_ids=input_ids,
                            attention_mask=attention_mask)

        # Use the last hidden state output from BERT encoder
        last_hidden_state = outputs[0]


        # Apply attention to the BERT output
        attn_output, _ = self.attention(last_hidden_state.transpose(0, 1), last_hidden_state.transpose(0, 1),
                                        last_hidden_state.transpose(0, 1))
        attn_output = attn_output.transpose(0, 1)


        # Apply CNN layer to the hidden states
        cnn_output = self.cnn(attn_output.transpose(1, 2))
        
         # Apply residual block to the CNN output
        res_output = self.res_block(cnn_output)

        # Apply Bidirectional GRU layer to the CNN output
        gru_output, _ = self.gru(res_output.transpose(1, 2)) # 添加一个双向GRU层
        
        lstm_output, _ = self.lstm(gru_output)
        
        max_pool_output = self.adaptive_max_pool(lstm_output.transpose(1, 2)).squeeze(-1) # 添加一个自适应最大池化层
        
        logits = self.classifier(max_pool_output)
        
        
        return logits
    

def initialize_model(train_dataloader, device, epochs=2):
    """
    初始化我们的bert，优化器还有学习率，epochs就是训练次数
    """
    # 初始化我们的Bert分类器
    bert_classifier = BertClassifier()
    # 用GPU运算
    bert_classifier.to(device)
    # 创建优化器
    optimizer = AdamW(bert_classifier.parameters(),
                      lr=5e-6,  # 默认学习率
                      eps=1e-8  # 默认精度
                      )
    # 训练的总步数
    total_steps = len(train_dataloader) * epochs
    # 学习率预热
    scheduler = get_linear_schedule_with_warmup(optimizer,
                                                num_warmup_steps=0,  # Default value
                                                num_training_steps=total_steps)
    return bert_classifier, optimizer, scheduler


# 训练模型
def train(model, train_dataloader, optimizer, scheduler, device, test_dataloader=None, epochs=2, evaluation=False):
    # 开始训练循环
    best_acc = 0
    for epoch_i in range(epochs):
        # =======================================
        #               Training
        # =======================================
        # 表头
        print(f"{'Epoch':^7} | {'每40个Batch':^9} | {'训练集 Loss':^12} | {'测试集 Loss':^10} | {'测试集准确率':^9} | {'时间':^9}")
        print("-" * 80)

        # 测量每个epoch经过的时间
        t0_epoch, t0_batch = time.time(), time.time()

        # 在每个epoch开始时重置跟踪变量
        total_loss, batch_loss, batch_counts = 0, 0, 0



        # 把model放到训练模式
        model.train()

        # 分batch训练
        for step, batch in enumerate(train_dataloader):
            batch_counts += 1
            # 把batch加载到GPU
            b_input_ids, b_attn_mask, b_labels = tuple(t.to(device) for t in batch)
            # print(b_labels.shape)
            # 归零导数
            model.zero_grad()
            # 真正的训练
            logits = model(b_input_ids, b_attn_mask)
            # print(logits.shape)
            # 计算loss并且累加
            # 实体化loss function
            loss_fn = nn.CrossEntropyLoss()  # 交叉熵
            loss = loss_fn(logits, b_labels.long())

            batch_loss += loss.item()
            total_loss += loss.item()
            # 反向传播
            loss.backward()
            # 归一化，防止梯度爆炸
            torch.nn.utils.clip_grad_norm_(model.parameters(), 1.0)
            # 更新参数和学习率
            optimizer.step()
            scheduler.step()

            # Print每40个batch的loss和time
            if (step % 40 == 0 and step != 0) or (step == len(train_dataloader) - 1):
                # 计算40个batch的时间
                time_elapsed = time.time() - t0_batch

                # Print训练结果
                print(
                    f"{epoch_i + 1:^7} | {step:^10} | {batch_loss / batch_counts:^14.6f} | {'-':^12} | {'-':^13} | {time_elapsed:^9.2f}")

                # 重置batch参数
                batch_loss, batch_counts = 0, 0
                t0_batch = time.time()
        # 计算平均loss 这个是训练集的loss
        avg_train_loss = total_loss / len(train_dataloader)

        print("-" * 80)
        # =======================================
        #               Evaluation
        # =======================================
        if evaluation:
            # 每个epoch之后评估一下性能
            # 在我们的验证集/测试集上.
            test_loss, test_accuracy = evaluate(model, device, test_dataloader)
            # Print 整个训练集的耗时
            time_elapsed = time.time() - t0_epoch

            print(
                f"{epoch_i + 1:^7} | {'-':^10} | {avg_train_loss:^14.6f} | {test_loss:^12.6f} | {test_accuracy:^12.2f}% | {time_elapsed:^9.2f}")
            print("-" * 80)
            if round(test_accuracy, 4) > best_acc:
                best_acc = round(test_accuracy, 4)
                model_to_save = model.module if hasattr(model, 'module') else model
                output_dir = "models"
                if not os.path.exists(output_dir):
                    os.makedirs(output_dir)
                torch.save(model_to_save.state_dict(), "./models/bert.bin")
        print("now_best_acc: " + str(best_acc))
        print("\n")


# 在测试集上面来看看我们的训练效果
def evaluate(model, device, test_dataloader,save_result =False):
    """
    在每个epoch后验证集上评估model性能
    """
    # model放入评估模式
    model.eval()

    # 准确率和误差
    # test_accuracy = []
    test_loss = []

    # 用列表存下每个batch的pred和test_label
    pred_list = []
    test_label = []

    # 验证集上的每个batch
    for batch in test_dataloader:
        # 放到GPU上
        b_input_ids, b_attn_mask, b_labels = tuple(t.to(device) for t in batch)

        # 计算结果，不计算梯度
        with torch.no_grad():
            logits = model(b_input_ids, b_attn_mask)  # 放到model里面去跑，返回验证集的ouput就是一行三列的
            # label向量可能性，这个时候还没有归一化所以还不能说是可能性，反正归一化之后最大的就是了
        # 实体化loss function
        loss_fn = nn.CrossEntropyLoss()  # 交叉熵
        # 计算误差
        loss = loss_fn(logits, b_labels.long())
        test_loss.append(loss.item())

        # get预测结果，这里就是求每行最大的索引咯，然后用flatten打平成一维:
        preds = torch.argmax(logits, dim=1).flatten()  # 返回一行中最大值的序号

        # 计算准确率，这个就是俩比较，返回相同的个数, .cpu().numpy()就是把tensor从显卡上取出来然后转化为numpy类型的矩阵好用方法
        # 最后mean因为直接bool形了，也就是如果预测和label一样那就返回1，正好是正确的个数，求平均就是准确率了
        # accuracy = (preds == b_labels).cpu().numpy().mean() * 100
        # test_accuracy.append(accuracy)
        for data in preds.cpu().numpy():
            pred_list.append(data)

        for data in b_labels.cpu().numpy():
            test_label.append(data)
        # pred_list.append(preds.cpu().numpy())
        # test_label.append(b_labels.cpu().numpy())

    # 计算整体的平均正确率和loss
    val_loss = np.mean(test_loss)
    # val_accuracy = np.mean(test_accuracy)
    if save_result:
        df = pd.DataFrame()
        df["label"] = test_label
        df["preds"] = pred_list
        df.to_csv("result.csv",index=False)
    return val_loss, Accuracy.model_performance(pred_list, test_label)


def prepareData(args):
    X_train, X_test, y_train, y_test = \
        dataset_load.load_data(args.train_data_file, args.test_data_file)
    tokenizer = RobertaTokenizer.from_pretrained("cardiffnlp/twitter-roberta-base-sentiment")
    encoded_comment = [tokenizer.encode(sent, add_special_tokens=True) for sent in X_train]
    # 文本最大长度
    MAX_LEN = max([len(sent) for sent in encoded_comment])
    print(MAX_LEN)
    train_inputs, train_masks = preprocessing_for_bert(X_train, tokenizer, MAX_LEN)
    test_inputs, test_masks = preprocessing_for_bert(X_test, tokenizer, MAX_LEN)
    train_labels = torch.tensor(y_train)
    test_labels = torch.tensor(y_test)

    # 用于BERT微调, batch size 16 or 32较好.
    batch_size = args.batch_size

    # 给训练集创建 DataLoader
    train_data = TensorDataset(train_inputs, train_masks, train_labels)
    train_sampler = RandomSampler(train_data)
    train_dataloader = DataLoader(train_data, sampler=train_sampler, batch_size=batch_size,drop_last=True )

    # 给验证集创建 DataLoader
    test_data = TensorDataset(test_inputs, test_masks, test_labels)
    test_sampler = SequentialSampler(test_data)
    test_dataloader = DataLoader(test_data, sampler=test_sampler, batch_size=batch_size)

    return train_dataloader, test_dataloader


def predict(model_path, device, text):
    model = BertClassifier()
    model.load_state_dict(torch.load(model_path, map_location=device))
    # tokenizer = RobertaTokenizer.from_pretrained('roberta-base')
    # tokenizer = AutoTokenizer.from_pretrained("microsoft/deberta-base")
    tokenizer = RobertaTokenizer.from_pretrained("cardiffnlp/twitter-roberta-base-sentiment")
    X_train = [preprocess.preprocess_text(text)]
    encoded_comment = [tokenizer.encode(sent, add_special_tokens=True) for sent in X_train]
    # 文本最大长度
    MAX_LEN = max([len(sent) for sent in encoded_comment])
    print(MAX_LEN)
    train_inputs, train_masks = preprocessing_for_bert(X_train, tokenizer, MAX_LEN=MAX_LEN)
    # model放入评估模式
    model.eval()
    b_input_ids = train_inputs.to(device)

    b_attn_mask = train_masks.to(device)

    # 用列表存下每个batch的pred和test_label

    # 计算结果，不计算梯度
    with torch.no_grad():
        logits = model(b_input_ids, b_attn_mask)  # 放到model里面去跑，返回验证集的ouput就是一行三列的

    # get预测结果，这里就是求每行最大的索引咯，然后用flatten打平成一维:
    preds = torch.argmax(logits, dim=1).flatten()  # 返回一行中最大值的序号

    return preds.cpu().item()


def predictList(model_path, device, texts):
    res = []
    for t in texts:
        res.append([t, predict(model_path, device, t)])
    return res


def main():
    parser = argparse.ArgumentParser()
    ## parameters
    parser.add_argument("--train_data_file", default="train3098itemPOLARITY.xlsx", type=str, required=False,
                        help="The input training data file (a csv file).")
    parser.add_argument("--test_data_file", default="test1326itemPOLARITY.xlsx", type=str, required=False,
                        help="The input testing data file (a csv file).")
    parser.add_argument("--batch_size", default=16, type=int,
                        help="Batch size per GPU/CPU for training.")
    parser.add_argument("--do_train", action='store_true', default=False,
                        help="Whether to run training.")
    parser.add_argument("--do_test", action='store_true', default=True,
                        help="Whether to run training.")
    parser.add_argument("--model_path", default="./models/bert.bin", type=str, required=False,
                        help="The input testing data file (a csv file).")
    args = parser.parse_args()
    ## 获取数据集信息

    if torch.cuda.is_available():
        device = torch.device("cuda")
        print(f'There are {torch.cuda.device_count()} GPU(s) available.')
        print('Device name:', torch.cuda.get_device_name(0))

    else:
        print('No GPU available, using the CPU instead.')
        device = torch.device("cpu")
    train_dataloader, test_dataloader = prepareData(args)

    bert_classifier, optimizer, scheduler = initialize_model(train_dataloader, device, epochs=1000)
    # print("Start training and validation:\n")
    if args.do_train:
        print("Start training\n")
        train(bert_classifier, train_dataloader, optimizer, scheduler, device, test_dataloader, epochs=1000,
              evaluation=True)
        net = BertClassifier()
        print("Total number of paramerters in networks is {}  ".format(sum(x.numel() for x in net.parameters())))
    if args.do_test:
        bert_classifier.load_state_dict(torch.load(args.model_path, map_location=device))
        evaluate(bert_classifier,device,test_dataloader,True)


if __name__ == '__main__':
    main()
