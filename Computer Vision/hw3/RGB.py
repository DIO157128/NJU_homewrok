import cv2
import numpy as np
import os

def calculate_histogram(image, bins):
    # 将图像转换为RGB颜色空间
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    # 计算颜色直方图
    hist = cv2.calcHist([image], [0, 1, 2], None, bins, [0, 256, 0, 256, 0, 256])
    # 归一化直方图
    hist = cv2.normalize(hist, hist).flatten()
    return hist

def image_search(query_image, dataset_folder, num_results):
    # 加载检索请求图像
    query = cv2.imread(query_image)
    # 计算检索请求图像的颜色直方图
    query_hist = calculate_histogram(query, [8, 8, 8])

    results = []
    # 遍历数据集中的图像
    for filename in os.listdir(dataset_folder):
        # 加载图像
        image_path = os.path.join(dataset_folder, filename)
        image = cv2.imread(image_path)
        # 计算当前图像的颜色直方图
        image_hist = calculate_histogram(image, [8, 8, 8])
        # 计算与检索请求图像的直方图的巴氏距离
        distance = cv2.compareHist(query_hist, image_hist, cv2.HISTCMP_BHATTACHARYYA)
        # 将图像路径和对应的距离添加到结果列表中
        results.append((image_path, distance))

    # 根据距离进行排序
    results = sorted(results, key=lambda x: x[1])

    # 返回前num_results个结果
    return results[:num_results]

# 设置检索请求图像路径和数据集文件夹路径
query_images_folder = './search request'
dataset_folder = './pictures'

# 循环处理每个检索请求图像
for query_image in os.listdir(query_images_folder):
    # 打印当前检索请求图像的名称
    print("Query Image:", query_image)
    # 执行图像检索
    results = image_search(os.path.join(query_images_folder, query_image), dataset_folder, 3)
    # 打印检索结果
    for i, result in enumerate(results):
        print("Result", i+1, ":", result[0])
