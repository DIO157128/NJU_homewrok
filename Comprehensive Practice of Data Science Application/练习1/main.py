import os
import torch
import torch.optim as optim
import tqdm
from torchvision.models.detection import fasterrcnn_resnet50_fpn
from torchvision.transforms import functional as F
from torch.utils.data import DataLoader, Dataset
from xml.etree import ElementTree as ET
from PIL import Image
import torch.nn as nn
import numpy as np

def calculate_iou(box1, box2):
    """
    计算两个框的交并比（IoU）。
    box1, box2: [x1, y1, x2, y2]，分别表示两个框的左上角和右下角坐标。
    """
    x1, y1, x2, y2 = box1
    x1_, y1_, x2_, y2_ = box2

    # 计算交叉区域的坐标
    x_intersection = max(0, min(x2, x2_) - max(x1, x1_))
    y_intersection = max(0, min(y2, y2_) - max(y1, y1_))

    # 计算两个框的面积
    area_box1 = (x2 - x1) * (y2 - y1)
    area_box2 = (x2_ - x1_) * (y2_ - y1_)

    # 计算交并比
    iou = (x_intersection * y_intersection) / float(area_box1 + area_box2 - x_intersection * y_intersection + 1e-16)

    return iou

def calculate_ap(ground_truth_boxes, predicted_boxes, confidence_scores, iou_threshold=0.5):
    """
    计算单个图像的平均精度（AP）。
    ground_truth_boxes: 实际框的坐标列表，每个实际框为 [x1, y1, x2, y2]。
    predicted_boxes: 预测框的坐标列表，每个预测框为 [x1, y1, x2, y2]。
    confidence_scores: 预测框的置信度分数列表。
    iou_threshold: 用于判断预测框是否与实际框匹配的IoU阈值。
    """
    num_predicted_boxes = len(predicted_boxes)
    num_ground_truth_boxes = len(ground_truth_boxes)
    true_positives = np.zeros(num_predicted_boxes)
    false_positives = np.zeros(num_predicted_boxes)
    precision = np.zeros(num_predicted_boxes)
    recall = np.zeros(num_predicted_boxes)

    # 标记预测框是否被匹配
    is_box_matched = np.zeros(num_ground_truth_boxes)

    # 按照置信度分数对预测框进行排序
    sorted_indices = np.argsort(-confidence_scores)

    for i in range(num_predicted_boxes):
        pred_box = predicted_boxes[sorted_indices[i]]
        max_iou = -1
        matched_ground_truth_idx = -1

        for j in range(num_ground_truth_boxes):
            if not is_box_matched[j]:
                iou = calculate_iou(pred_box, ground_truth_boxes[j])
                if iou > max_iou:
                    max_iou = iou
                    matched_ground_truth_idx = j

        # 判断预测框是否与实际框匹配
        if max_iou >= iou_threshold:
            true_positives[i] = 1
            is_box_matched[matched_ground_truth_idx] = 1
        else:
            false_positives[i] = 1

        # 计算精度和召回率
        precision[i] = np.sum(true_positives) / (np.sum(true_positives) + np.sum(false_positives))
        recall[i] = np.sum(true_positives) / num_ground_truth_boxes

    # 计算平均精度（AP）通过计算不同召回率下的精度并求平均
    ap = np.mean(precision)

    return ap
# 自定义数据集类
class CustomDataset(Dataset):
    def __init__(self, xml_folder, image_folder, target_size=(800, 600)):
        self.xml_files = [os.path.join(xml_folder, file) for file in os.listdir(xml_folder) if file.endswith('.xml')]
        self.image_folder = image_folder
        self.target_size = target_size

    def __len__(self):
        return len(self.xml_files)

    def __getitem__(self, idx):
        xml_file = self.xml_files[idx]
        tree = ET.parse(xml_file)
        root = tree.getroot()

        image_path = xml_file.replace('Annotations','JPEGImages').replace('.xml','.jpg')
        original_image = Image.open(image_path).convert("RGB")

        # 获取原始图像的尺寸
        original_width, original_height = original_image.size

        # 调整图像大小
        resized_image = F.resize(original_image, self.target_size)

        # 计算调整比例
        width_ratio = self.target_size[0] / original_width
        height_ratio = self.target_size[1] / original_height

        # 解析bounding box信息并调整坐标
        boxes = []
        labels = []
        for obj in root.findall('object'):
            name = obj.find('name').text
            xmin = int(obj.find('bndbox/xmin').text)
            ymin = int(obj.find('bndbox/ymin').text)
            xmax = int(obj.find('bndbox/xmax').text)
            ymax = int(obj.find('bndbox/ymax').text)

            # 调整坐标
            xmin *= width_ratio
            ymin *= height_ratio
            xmax *= width_ratio
            ymax *= height_ratio

            boxes.append([xmin, ymin, xmax, ymax])
            labels.append(1)  # 杆塔类别的标签为1

        targets = {
            "boxes": torch.tensor(boxes, dtype=torch.float32),
            "labels": torch.tensor(labels, dtype=torch.int64)
        }

        return F.to_tensor(resized_image), targets



def evaluate_model(model, data_loader, iou_threshold=0.5):
    model.eval()
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model.to(device)

    all_predictions = []
    all_ground_truths = []

    with torch.no_grad():
        for images, targets in tqdm.tqdm(data_loader):
            images = list(image.to(device) for image in images)
            targets = [{k: v.to(device) for k, v in targets.items()}]  # 正确处理targets列表

            predictions = model(images)

            for i, prediction in enumerate(predictions):
                predicted_boxes = prediction["boxes"].cpu().numpy()
                confidence_scores = prediction["scores"].cpu().numpy()
                ground_truth_boxes = targets[0]["boxes"][i].cpu().numpy()

                ap = calculate_ap(ground_truth_boxes, predicted_boxes, confidence_scores, iou_threshold)
                all_predictions.append(ap)
                all_ground_truths.append(1)  # Assuming there's a true positive for each prediction
    mean_ap = np.mean(all_predictions)
    return mean_ap


def train_model(model, train_data_loader, validation_data_loader, optimizer, num_epochs=10):
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model.to(device)

    best_ap = 0
    for epoch in range(num_epochs):
        model.train()
        total_loss = 0.0
        trnum = 0
        for images, targets in tqdm.tqdm(train_data_loader):
            images = list(image.to(device) for image in images)
            targets = [{k: v[0].to(device) for k, v in targets.items()}]
            optimizer.zero_grad()
            loss_dict = model(images, targets)
            losses = sum(loss for loss in loss_dict.values())
            losses.backward()
            optimizer.step()
            total_loss += losses.item()
            trnum+=1
        # Calculate validation loss
        val_ap = evaluate_model(model,validation_data_loader)

        # Print current loss
        print(
            f"Epoch {epoch + 1}/{num_epochs}, Training Loss: {total_loss / len(train_data_loader)}, Validation AP: {val_ap}")

        # Save the model if it has the best validation loss
        if val_ap > best_ap:
            best_ap = val_ap
            torch.save(model.state_dict(), 'best_model.pth')

    print("Training complete.")


# 设置文件夹路径
xml_folder = "练习1数据集/Annotations/train"
image_folder = "练习1数据集/JPEGImages/train"
# 设置验证集文件夹路径
val_xml_folder = "练习1数据集/Annotations/val"
val_image_folder = "练习1数据集/JPEGImages/val"

test_image_folder = "练习1数据集/JPEGImages/test"

# 构建验证集的数据集和数据加载器
val_dataset = CustomDataset(val_xml_folder, val_image_folder)
val_data_loader = DataLoader(val_dataset, batch_size=1, shuffle=False)

# 构建数据集和数据加载器
dataset = CustomDataset(xml_folder, image_folder)
data_loader = DataLoader(dataset, batch_size=1, shuffle=True)

# 使用预训练的 Faster R-CNN 模型
model = fasterrcnn_resnet50_fpn(pretrained=True)



# 定义优化器
optimizer = optim.SGD(model.parameters(), lr=0.005, momentum=0.9)
# 训练模型
train_model(model, data_loader, val_data_loader,optimizer, num_epochs=10)

# 加载训练好的模型
model = fasterrcnn_resnet50_fpn(pretrained=False)  # 不使用预训练权重
model.load_state_dict(torch.load('best_model.pth'))
model.eval()

# 输入图像文件夹路径
image_folder = "练习1数据集/JPEGImages/test"

# 输出结果文件夹路径
output_folder = "output"

# 设备选择
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model.to(device)

# 处理测试集图像并保存结果
with torch.no_grad():
    # 遍历测试集中的图像文件
    for image_file in os.listdir(image_folder):
        # 加载图像
        image_path = os.path.join(image_folder, image_file)
        original_image = Image.open(image_path).convert("RGB")
        image_tensor = F.to_tensor(original_image).unsqueeze(0).to(device)  # 添加批次维度并移动到设备上

        # 进行推断
        predictions = model(image_tensor)[0]  # 只获取第一个批次的预测结果

        # 获取预测框信息并保存到文本文件
        output_file = os.path.join(output_folder, f"{os.path.splitext(image_file)[0]}.txt")
        with open(output_file, "w") as file:
            for score, label, box in zip(predictions["scores"], predictions["labels"], predictions["boxes"]):
                box = box.tolist()  # 将Tensor转换为列表
                line = f"tower {score:.4f} {box[0]:.2f} {box[1]:.2f} {box[2]:.2f} {box[3]:.2f}\n"
                file.write(line)