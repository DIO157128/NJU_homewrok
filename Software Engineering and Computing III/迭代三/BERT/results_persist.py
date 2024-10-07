import os
import time

import numpy as np
from sklearn.metrics import confusion_matrix, accuracy_score


def model_performance(preds, test_label):
    num_calss = set(preds)

    conf_matrix = confusion_matrix(test_label, preds)

    os.makedirs(f"output", exist_ok=True)
    save_path = "output/log_" + str(time.time()) + ".txt"

    file = open(save_path, 'w', encoding="utf-8")
    file.write("混淆矩阵输出如下:\n")

    for row in conf_matrix:
        file.write('\t'.join([str(elem) for elem in row]))
        file.write('\n')

    for j in range(len(num_calss)):
        TP = conf_matrix[j, j]
        FN = conf_matrix[j, :].sum() - TP
        FP = conf_matrix[:, j].sum() - TP

        recall = TP / (TP + FN)
        precision = TP / (TP + FP)
        f1score = 2 * (precision * recall) / (precision + recall)

        file.write("-------------------------\n")
        file.write("recall: {}\n".format(recall))
        file.write("precision: {}\n".format(precision))
        file.write("f1score: {}\n".format(f1score))
        file.write("-------------------------\n")

    accuracy = accuracy_score(test_label, preds)

    file.write("总体的正确率是： {}".format(accuracy))
    file.close()

    return accuracy * 100


if __name__ == "__main__":
    model_performance([1,1,0,0,2,2], [1,1,2,0,1,2])









