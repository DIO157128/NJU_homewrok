import math

import numpy
import pandas as pd
import numpy as np
import scipy.stats as stats
import csv
import matplotlib.pyplot as plt
def readCol(col,path):
    df = pd.read_csv(path)
    to_check = np.array(df.iloc[:, col-1:col])
    res = []
    for line in to_check:
        res.append(line[0])
    return np.array(res)
def readRow(row,path):
    df = pd.read_csv(path)
    to_check = np.array(df.iloc[row-1:row, :])
    res = to_check[0]
    return np.array(res)
def generateBox(col,path):
    to_check = readCol(col,path)
    plt.boxplot(to_check)
    plt.show()
    Q1 = np.quantile(a=to_check, q=0.25)
    Q3 = np.quantile(a=to_check, q=0.75)
    # 计算 四分位差
    QR = Q3 - Q1
    # 下限 与 上线
    low_limit = Q1 - 1.5 * QR
    up_limit = Q3 + 1.5 * QR
    print('下限为：', low_limit)
    print('上限为：', up_limit)
    print('异常值有：')
    print(to_check[(to_check < low_limit) + (to_check > up_limit)])
    return low_limit,up_limit,to_check[(to_check < low_limit) + (to_check > up_limit)]
def generateBoxAll(path):
    dt = pd.read_csv(path)
    dt.boxplot()
    plt.show()
def gaussCheck(col,path):
    to_check = readCol(col,path)
    df = pd.DataFrame(to_check, columns=['value'])
    u = df['value'].mean()  # 计算均值
    std = df['value'].std()  # 计算标准差
    print(stats.kstest(df['value'], 'norm', (u, std)))
    # .kstest方法：KS检验，参数分别是：待检验的数据，检验方法（这里设置成norm正态分布），均值与标准差
    # 结果返回两个值：statistic → D值，pvalue → P值
    # p值大于0.05，为正态分布
def checkAll(path):
    to_check = readCol(1,path)
    for i in range(1, 11):
        to_check = np.append(to_check, readCol(i,path))
    df = pd.DataFrame(to_check, columns=['value'])
    u = df['value'].mean()  # 计算均值
    std = df['value'].std()  # 计算标准差
    print(stats.kstest(df['value'], 'norm', (u, std)))
    plt.boxplot(to_check)
    plt.show()
    Q1 = np.quantile(a=to_check, q=0.25)
    Q3 = np.quantile(a=to_check, q=0.75)
    # 计算 四分位差
    QR = Q3 - Q1
    # 下限 与 上线
    low_limit = Q1 - 1.5 * QR
    up_limit = Q3 + 1.5 * QR
    print('下限为：', low_limit)
    print('上限为：', up_limit)
    print('异常值有：')
    print(to_check[(to_check < low_limit) + (to_check > up_limit)])
    return low_limit,up_limit,to_check[(to_check < low_limit) + (to_check > up_limit)]
def checkAllSep(path):
    for i in range(1,11):
        gaussCheck(i,path)
        generateBox(i,path)
        print("*"*20)
def Zscore(path):
    all_data = pd.read_csv(path)
    to_check = readCol(1, path)
    for i in range(1, 11):
        to_check = np.append(to_check, readCol(i, path))
    df = pd.DataFrame(to_check, columns=['value'])
    u_all = df['value'].mean()  # 计算均值
    std_all = df['value'].std()  # 计算标准差
    for i in range(1,11):
        to_check = readCol(i,path)
        df = pd.DataFrame(to_check, columns=['value'])
        u = df['value'].mean()  # 计算均值
        std = df['value'].std()  # 计算标准差
        for j in range(200):
            all_data.iloc[j-1,i-1]=(all_data.iloc[j-1,i-1]-u)/std*std_all+u_all
    all_data.to_csv(r"data_after_normalization.csv", mode='w', index=False)
def calAve(path):
    ave = []
    for i in range(1,201):
        row = readRow(i,path)
        row_ave = sum(row)/len(row)
        ave.append(row_ave)
    df = pd.read_csv("data_after_normalization.csv")
    df['平均值'] = ave
    df.to_csv(r"data_after_normalization.csv", mode='w', index=False)
def setPrize(path):
    res = open("prize.txt",'w',encoding="utf-8")
    ave = readCol(11,path)
    to_sort = []
    idx=1
    for i in ave:
        to_sort.append([idx,i])
        idx+=1
    to_sort.sort(key=lambda x: (-x[1]))
    res.write("获一等奖的组为：\n")
    for i in range(0,20):
        res.write("第{}组".format(str(to_sort[i][0]))+"，得分{}".format(str(to_sort[i][1]))+'\n')
    res.write("获二等奖的组为：\n")
    for i in range(20,50):
        res.write("第{}组".format(str(to_sort[i][0]))+"，得分{}".format(str(to_sort[i][1])+'\n'))
    res.write("获三等奖的组为：\n")
    for i in range(51, 100):
        res.write("第{}组".format(str(to_sort[i][0]))+"，得分{}".format(str(to_sort[i][1]))+'\n')
def modifyData(path,path2):
    all_data = pd.read_csv(path)
    for i in range(1,11):
        low,up,invalid = generateBox(i,path)
        low=math.ceil(low)
        up =math.floor(up)
        for j in range(200):
            if all_data.iloc[j - 1, i - 1] in invalid:
                if all_data.iloc[j - 1, i - 1]>up:
                    all_data.iloc[j - 1, i - 1] = up
                else:
                    all_data.iloc[j - 1, i - 1] = low
    all_data.to_csv(path2, mode='w', index=False)
    # low_all, up_all, invalid_all = checkAll(r"data_after_modification.csv")
    # low_all = math.ceil(low_all)
    # up_all = math.floor(up_all)
    # for i in range(1, 11):
    #     for j in range(200):
    #         if all_data.iloc[j - 1, i - 1] in invalid_all:
    #             if all_data.iloc[j - 1, i - 1] > up_all:
    #                 all_data.iloc[j - 1, i - 1] = up_all
    #             else:
    #                 all_data.iloc[j - 1, i - 1] = low_all
    # all_data.to_csv(r"data_after_modification.csv", mode='w', index=False)
if __name__=="__main__":
    # checkAllSep("data.csv")  # 检查每个评委的打分情况是否符合正态分布
    # modifyData("data.csv","data_after_modification.csv")
    # checkAllSep("data_after_modification.csv")
    # generateBoxAll("data_after_modification.csv")
    # Zscore("data_after_modification.csv")
    # modifyData("data_after_normalization.csv","data_after_normalization.csv")
    checkAllSep("data_after_normalization.csv")
    # generateBoxAll("data_after_normalization.csv")
    # calAve("data_after_normalization.csv")
    # setPrize("data_after_normalization.csv")




