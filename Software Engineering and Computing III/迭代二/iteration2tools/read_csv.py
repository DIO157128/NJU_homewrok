import os

import pandas as pd
import numpy as np
def sc(name):
    df = pd.read_csv("sc_{}.csv".format(name))
    # text = df["Comment (All)"].tolist()
    # f = open("sc_{}.txt".format(name),"a",encoding="utf-8")
    # for t in text:
    #     f.write(t+"\n")
    # f.close()
    # os.system("java -jar ./sentistrength_jar/sentistrength.jar sentidata ./sentistrength_jar/SentStrength_Data/ input sc_{}.txt trinary".format(name))
    f = open("sc_{}0_out.txt".format(name), 'r', encoding="utf-8")
    score = []
    lines = f.readlines()
    for l in lines[2:]:
        score.append(l.split()[0])
    score = np.array(score)
    df["run score"] = score
    df.to_csv("sc_{}_res.csv".format(name),index=False)
    os.remove("sc_{}0_out.txt".format(name))
    os.remove("sc_{}.txt".format(name))
def se(name):
    df = pd.read_csv("se_{}.csv".format(name))
    text = df["Text"].tolist()
    f = open("se_{}.txt".format(name),"a",encoding="utf-8")
    for t in text:
        f.write(t+"\n")
    f.close()
    os.system("java -jar ./sentistrength_jar/sentistrength.jar sentidata ./sentistrength_jar/SentStrength_Data/ input se_{}.txt trinary".format(name))
    f = open("se_{}0_out.txt".format(name), 'r', encoding="utf-8")
    score = []
    lines = f.readlines()
    for l in lines[2:]:
        score.append(l.split()[0])
    score = np.array(score)
    df["run score"] = score
    df.to_csv("se_{}_res.csv".format(name),index=False)
    os.remove("se_{}0_out.txt".format(name))
    os.remove("se_{}.txt".format(name))
def createsc4ana(name):
    df = pd.read_csv("sc_{}_res.csv".format(name))
    pos = df["mean pos"].tolist()
    neg = df["mean neg"].tolist()
    arti_score = []
    index = df["文本序号"]
    text = df["Comment (All)"]
    score = df["run score"]
    for p,n in zip(pos,neg):
        res = 0
        if (p+n)>0:
            res = 1
        elif (p+n)<0:
            res = -1
        else:
            res = 0
        arti_score.append(res)
    df = pd.DataFrame()
    df["index"] = index
    df["text"] = text
    df["人工"] = arti_score
    df["run score"] = score
    df.to_csv("./analysis_data/sc_{}.csv".format(name),index=False)
if __name__ == "__main__":
    createsc4ana("bbc1000")
    createsc4ana("myspace1041")