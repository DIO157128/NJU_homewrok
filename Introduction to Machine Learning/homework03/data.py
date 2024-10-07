import pandas
import numpy as np
import pandas as pd
from scipy.stats import pearsonr
def dataEncode():
    df_train = pd.read_csv("filtered_data/training.csv")
    df_val = pd.read_csv("filtered_data/validation.csv")
    df_test = pd.read_csv("filtered_data/testing.csv")
    df_train.drop_duplicates(inplace=True)
    df_val.drop_duplicates(inplace=True)
    df_test.drop_duplicates(inplace=True)
    drug_name = df_train["drugName"].tolist() + df_val["drugName"].tolist() + df_test["drugName"].tolist()
    condition = df_train["condition"].tolist() + df_val["condition"].tolist() + df_test["condition"].tolist()
    sideEffects = df_train["sideEffects"].tolist() + df_val["sideEffects"].tolist() + df_test["sideEffects"].tolist()
    dict_drug = {}
    count = 0
    for i in sorted(set(drug_name)):
        dict_drug[i] = count
        count+=1
    dict_condition = {}
    count = 0
    for i in sorted(set(condition)):
        dict_condition[i] = count
        count += 1
    dict_effect = {}
    count = 0
    for i in sorted(set(sideEffects)):
        dict_effect[i] = count
        count += 1
    drug_name_train = [dict_drug[x] for x in df_train["drugName"].tolist()]
    drug_name_val = [dict_drug[x] for x in df_val["drugName"].tolist()]
    drug_name_test = [dict_drug[x] for x in df_test["drugName"].tolist()]
    condition_train = [dict_condition[x] for x in df_train["condition"].tolist()]
    condition_val = [dict_condition[x] for x in df_val["condition"].tolist()]
    condition_test = [dict_condition[x] for x in df_test["condition"].tolist()]
    date_train = pd.to_datetime(df_train["date"])
    date_val = pd.to_datetime(df_val["date"])
    date_test = pd.to_datetime(df_test["date"])
    sideEffects_train = [dict_effect[x] for x in df_train["sideEffects"].tolist()]
    sideEffects_val = [dict_effect[x] for x in df_val["sideEffects"].tolist()]
    sideEffects_test = [dict_effect[x] for x in df_test["sideEffects"].tolist()]
    df_train["drugName"] = drug_name_train
    df_train["condition"] = condition_train
    df_train["date"] = date_train
    df_train["sideEffects"] = sideEffects_train
    df_val["drugName"] = drug_name_val
    df_val["condition"] = condition_val
    df_val["date"] = date_val
    df_val["sideEffects"] = sideEffects_val
    df_test["drugName"] = drug_name_test
    df_test["condition"] = condition_test
    df_test["date"] = date_test
    df_test["sideEffects"] = sideEffects_test
    df_train.to_csv("filtered_data/final_training.csv")
    df_val.to_csv("filtered_data/final_val.csv")
    df_test.to_csv("filtered_data/final_test.csv")
def pearson():
    df = pd.read_csv("filtered_data/final_training.csv")
    drug_name = df["drugName"]
    condition = df["condition"]
    side_effect = df["sideEffects"]
    ratings = df["rating"]
    useful = df["usefulCount"]
    print("drugname:{}".format(pearsonr(drug_name,ratings)))
    print("condition:{}".format(pearsonr(condition,ratings)))
    print("sideeffect:{}".format(pearsonr(side_effect,ratings)))
    print("usefulcount:{}".format(pearsonr(useful,ratings)))
if __name__=="__main__":
    # dataEncode()
    pearson()
