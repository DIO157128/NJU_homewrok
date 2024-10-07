import cv2
import numpy as np
import os

def calculate_shape_features(image):
    # 将图像转换为灰度图像
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    # 对图像进行阈值处理
    _, thresh = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)
    # 查找轮廓
    contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    # 计算图像的形状特征（轮廓面积和周长）
    area = cv2.contourArea(contours[0])
    perimeter = cv2.arcLength(contours[0], True)
    # 将形状特征组合为特征向量
    features = np.array([area, perimeter])
    return features

def image_search(query_image, dataset_folder, num_results):
    # 加载检索请求图像
    query = cv2.imread(query_image)
    # 计算检索请求图像的形状特征
    query_features = calculate_shape_features(query)

    results = []
    # 遍历数据集中的图像
    for filename in os.listdir(dataset_folder):
        # 加载图像
        image_path = os.path.join(dataset_folder, filename)
        image = cv2.imread(image_path)
        # 计算当前图像的形状特征
        image_features = calculate_shape_features(image)
        # 计算与检索请求图像的特征之间的欧氏距离
        distance = np.linalg.norm(query_features - image_features)
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
