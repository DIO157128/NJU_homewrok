import copy
import operator

import pandas as pd
import math

def loadDataSet(file_path):
    dataSet = pd.read_csv(file_path, delimiter=',')
    labelSet = list(dataSet.columns)[:-1]
    # print(labelSet)
    dataSet = dataSet.values
    # print(dataSet)
    return dataSet, labelSet


#计算香农熵
def calcShannonEnt(dataSet):
    #样本数量
    sample_size = len(dataSet)
    #计算每个label出现次数的字典
    labelCount = {}
    for sample in dataSet:
        current_label = sample[-1]
        if current_label not in labelCount.keys():
            labelCount[current_label] = 0
        labelCount[current_label] += 1

    shannonEnt = 0.0
    for key in labelCount.keys():
        prob = float(labelCount[key]) / sample_size
        shannonEnt -= prob*math.log(prob, 2)

    return shannonEnt

#按照给定特征划分数据集
#返回按照attribute属性划分，并且属性的值是value的子数据集，并把该属性及其值去掉
def splitDataSet(dataSet, axis, value):
    res = []
    for sample in dataSet:
        if(sample[axis] == value):
            reducedSample = list(sample[:axis])
            reducedSample.extend(sample[axis+1:])
            res.append(reducedSample)

    # print(res)
    return res

#按照给定特征划分数据集
#返回按照attribute属性划分，并且属性的值是value的子数据集，并把该属性及其值去掉
#改进，可以处理连续变化的值
def splitDataSet_c(dataSet, axis, value, LorR = 'L'):
    res = []
    if LorR == 'L':
        for sample in dataSet:
            if float(sample[axis]) < float(value):
                res.append(sample)
    else:
        for sample in dataSet:
            if float(sample[axis]) > float(value):
                res.append(sample)

    return res




#按照最大信息增益划分数据集
#返回用于切分的标签的索引值
#改进，处理连续值
def chooseBestFeatureToSplit_c(dataSet, labelProperty):
    feature_number = len(labelProperty)
    base_entropy = calcShannonEnt(dataSet)
    best_info_gain = 0.0
    best_feature = -1 #初始化最佳特征的索引值
    best_part_value = None

    for i in range(feature_number):
        features = [example[i] for example in dataSet]
        unique_feature = set(features)

        entropy = 0.0
        best_part_value_i = None
        if labelProperty[i] == 0: #处理离散化
            for value in unique_feature:
                subDataSet = splitDataSet(dataSet, i, value)
                prob = len(subDataSet) / float(len(dataSet))
                entropy += prob * calcShannonEnt(subDataSet)

        else: #处理连续的特征
            sorted_unique_feature = list(unique_feature)
            sorted_unique_feature.sort()
            # min_entropy = float("-inf")
            min_entropy = 999999999
            for j in range(len(sorted_unique_feature) - 1): #计算划分点
                part_value = (float(sorted_unique_feature[j]) + float(sorted_unique_feature[j + 1])) / 2
                dataSetLeft = splitDataSet_c(dataSet, i, part_value, 'L')
                dataSetRight = splitDataSet_c(dataSet, i, part_value, 'R')
                prob_left = len(dataSetLeft) / float(len(dataSet))
                prob_right = len(dataSetRight) / float(len(dataSet))
                entropy_temp = prob_left * calcShannonEnt(dataSetLeft) + prob_right * calcShannonEnt(dataSetRight)
                if entropy_temp < min_entropy:
                    min_entropy = entropy_temp
                    best_part_value_i = part_value
            entropy = min_entropy
        info_gain = base_entropy - entropy
        if info_gain > best_info_gain:
            best_info_gain = info_gain
            best_feature = i
            best_part_value = best_part_value_i
    return best_feature, best_part_value






#计算出现次数最多的标签的值，并返回该值
def majorCnt(classList):
    class_count = {}

    for sth in classList:
        if sth not in class_count.keys():
            class_count[sth] = 0

        class_count[sth] += 1

    sorted_class_count = sorted(class_count.items(), key=operator.itemgetter(1), reverse=True)
    return sorted_class_count[0][0]


#生成决策树
#改进， 可以处理连续
def createTree(dataSet, labels, labelProperty):
    class_list = [example[-1] for example in dataSet] #取出标签

    #如果集合中样本属于同一类别，则将node标记为该类叶节点
    if class_list.count(class_list[0]) == len(class_list):
        return class_list[0]

    #如果属性集为空或者样本在属性集上的取值相同，则将node标记为叶节点
    if len(dataSet[0]) == 1:
        return majorCnt(class_list)

    #选择最佳标签，返回标签索引
    best_feat, best_part_value = chooseBestFeatureToSplit_c(dataSet, labelProperty)

    #如果无法选出最优分类特征，返回次数最多的类别
    if best_feat == -1:
        return majorCnt(class_list)

    if(labelProperty[best_feat] == 0):
        best_feat_label = labels[best_feat]
        myTree = {best_feat_label: {}}
        labels_new = copy.copy(labels)
        labelPropertyNew = copy.copy(labelProperty)
        del(labels_new[best_feat])
        del(labelPropertyNew[best_feat])

        feat_values = [example[best_feat] for example in dataSet]
        unique_value = set(feat_values)

        for value in unique_value:
            subLabels = labels_new[:]
            subLabelProperty = labelPropertyNew[:]
            myTree[best_feat_label][value] = createTree(splitDataSet(dataSet, best_feat, value), subLabels, subLabelProperty)

    else: #对于连续值的处理，不删除特征，分别建立左右子树
        best_feat_label = labels[best_feat] + '<' + str(best_part_value)
        myTree = {best_feat_label:{}}
        subLabels = labels[:]
        subLabelsProperty = labelProperty[:]

        value_left = 'yes'
        myTree[best_feat_label][value_left] = createTree(splitDataSet_c(dataSet, best_feat, best_part_value, 'L'), subLabels, subLabelsProperty)

        value_right = 'no'
        myTree[best_feat_label][value_right] = createTree(splitDataSet_c(dataSet, best_feat, best_part_value, 'R'), subLabels, subLabelsProperty)
    return myTree


#使用决策树进行预测
def predict(inputTree, feat_labels, test_vec, labelProperty):
    firstStr = list(inputTree.keys())[0]
    first_label = firstStr
    lessIndex = str(firstStr).find('<')

    if(lessIndex) > -1:
        first_label = str(firstStr)[:lessIndex]
    secondDict = inputTree[firstStr]
    featIndex = feat_labels.index(first_label)
    class_label = None
    for key in secondDict.keys():
        if(labelProperty[featIndex] == 0):
            if test_vec[featIndex] == str(key):
                if type(secondDict[key]).__name__ == 'dict':
                    class_label = predict(secondDict[key], feat_labels, test_vec, labelProperty)
                else:
                    class_label = secondDict[key]
        else:
            part_value = float(str(firstStr)[lessIndex + 1:])
            if float(test_vec[featIndex]) < part_value:
                if type(secondDict['yes']).__name__ == 'dict':
                    class_label = predict(secondDict['yes'], feat_labels, test_vec, labelProperty)
                else:
                    class_label = secondDict['yes']
            else:
                if type(secondDict['no']).__name__ == 'dict':
                    class_label = predict(secondDict['no'], feat_labels, test_vec, labelProperty)
                else:
                    class_label = secondDict['no']

    return class_label




