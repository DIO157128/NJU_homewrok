import pickle

import pandas as pd
import tqdm
import ID3
from sklearn.metrics import f1_score
train_file_path = "./final_data/train.csv"
valid_file_path = "./final_data/val.csv"
test_file_path = "./final_data/test.csv"
if __name__ == "__main__":
    #使用训练集训练决策树
    dataSet, labelSet = ID3.loadDataSet(train_file_path)
    myTree = ID3.createTree(dataSet, labelSet, [1, 1])
    print(myTree)
    with open('tree.pickle', 'wb') as file:
        pickle.dump(myTree, file)
    #提取验证集的数据进行验证
    validDataSet, _ = ID3.loadDataSet(valid_file_path)
    val_vec_list = [[example[0], example[1]] for example in validDataSet]
    real_rating_list = [example[2] for example in validDataSet]

    #使用决策树获得预测rating_list
    predict_val_rating_list = []
    for val_vec in tqdm.tqdm(val_vec_list):
        predict_val_rating_list.append(ID3.predict(myTree, labelSet, val_vec, [1, 1]))

    micro_f1 = f1_score(real_rating_list, predict_val_rating_list, average="micro")
    macro_f1 = f1_score(real_rating_list, predict_val_rating_list, average="macro")

    print("Micro F1: ", micro_f1)
    print("Macro F1: ", macro_f1)

    testDataSet, _ = ID3.loadDataSet(test_file_path)
    test_vec_list = [[example[0], example[1]] for example in testDataSet]
    real_rating_list = [example[2] for example in testDataSet]

    #使用决策树获得预测rating_list
    predict_test_rating_list = []
    for test_vec in tqdm.tqdm(test_vec_list):
        predict_test_rating_list.append(ID3.predict(myTree, labelSet, test_vec, [1, 1]))
    df = pd.read_csv(test_file_path)
    df["rating"] = predict_test_rating_list
    df.to_csv("res.csv",index=False)
