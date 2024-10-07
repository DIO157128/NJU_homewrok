import random
from tqdm import tqdm
import numpy as np
import pandas as pd

global current_sequence
def selectRandom(m,n,s):
    # m是组数，n是评委数，s是重复的评委数量
    judges_to_select= [i for i in range(1,n+1)]
    group_sequence = []
    try:
        for group_index in range(m):
            judge_sequence = random.sample(judges_to_select,3)
            group_sequence.append(sorted(judge_sequence))
    except ValueError:
        return False
    return validityJudge(group_sequence,s)

def validityJudge(group_sequence,s):
    # 用于判断评委组合是否合法
    for i in range(len(group_sequence)):
        for j in range(len(group_sequence)):
            if i == j:
                continue
            else:
                tem1 = group_sequence[i]
                tem2 = group_sequence[j]
                same = 0
                for num1 in tem1:
                    if num1 in tem2:
                        same+=1
                if same>=s:
                    return False
    global current_sequence
    current_sequence=group_sequence
    return True

def generateRandomSample(it,m,n,s):
    # it是每个组合的迭代次数
    res = np.full((m, n), True, dtype=bool)
    seq_res = open('seq_res_{}.txt'.format(s),'w')
    for _m in range(m,m+1):
        for _n in range(n,n+1):
            flag=False
            for i in tqdm(range(it),desc="m:{} , n:{}".format(_m,_n)):
                if selectRandom(_m, _n, s):
                    flag=True
                    seq_res.write("Combination: m:{} , n:{}\n".format(_m,_n))
                    for idx in range(len(current_sequence)):
                        seq_res.write("group {} is judged by {},{},{}".format(idx+1,current_sequence[idx][0],current_sequence[idx][1],current_sequence[idx][2])+"\n")
                    seq_res.write("*"*20+'\n')
                    break
            res[_m-1][_n-1]=flag
            if flag:
                res[_m-1][_n-1:]=flag
                break
    pd.DataFrame(res).to_csv('res_matrix_{}.csv'.format(s))

def evaluateSeq(group_sequence):
    # 有x个重复评委的组数
    evaluation_res = [0,0,0,0]
    for i in range(len(group_sequence)):
        for j in range(len(group_sequence)):
            if i == j:
                continue
            else:
                tem1 = group_sequence[i]
                tem2 = group_sequence[j]
                same = 0
                for num1 in tem1:
                    if num1 in tem2:
                        same+=1
                evaluation_res[same]+=1
    score = -(evaluation_res[1]+4*evaluation_res[2]+9*evaluation_res[3])
    return score,group_sequence
def calBestSequence(m,n,it):
    score = -999999
    sequence =[]
    for i in tqdm(range(it)):
        judges_to_select = [i for i in range(1, n + 1)]
        group_sequence = []
        for group_index in range(m):
            judge_sequence = random.sample(judges_to_select, 3)
            group_sequence.append(sorted(judge_sequence))
        tem_score,tem_sequence = evaluateSeq(group_sequence)
        if tem_score>=score:
            score = tem_score
            sequence = tem_sequence
    print("Best Score:{}".format(score))
    print("One possible sequence is shown below:")
    for idx in range(len(sequence)):
        print("Group {} is judged by {},{},{}".format(idx+1,sequence[idx][0],sequence[idx][1],sequence[idx][2]))








if __name__=="__main__":
    # # generateRandomSample(10000, 30, 30, 3)
    calBestSequence(20,10,1000000)