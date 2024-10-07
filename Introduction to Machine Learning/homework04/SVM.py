import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.svm import SVC
from sklearn.metrics import confusion_matrix, accuracy_score, precision_score, recall_score
import matplotlib.pyplot as plt
# 读取Ionosphere数据集
url = "https://archive.ics.uci.edu/ml/machine-learning-databases/ionosphere/ionosphere.data"
ionosphere_data = pd.read_csv(url, header=None)
# 分割特征和目标变量
X = ionosphere_data.iloc[:, :-1]
y = ionosphere_data.iloc[:, -1]
# 将数据集分割为训练集和测试集
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
# 创建SVM模型
svm_model = SVC(kernel='linear', random_state=42)

# 拟合模型
svm_model.fit(X_train, y_train)
# 对测试集进行预测
y_pred = svm_model.predict(X_test)

# 计算混淆矩阵
cm = confusion_matrix(y_test, y_pred)

# 计算准确率
accuracy = accuracy_score(y_test, y_pred)

# 计算精确率
precision = precision_score(y_test, y_pred, pos_label='g')

# 计算召回率
recall = recall_score(y_test, y_pred, pos_label='g')

# 打印混淆矩阵、准确率、精确率和召回率
print("Confusion Matrix:")
print(cm)
print("Accuracy:", accuracy)
print("Precision:", precision)
print("Recall:", recall)
