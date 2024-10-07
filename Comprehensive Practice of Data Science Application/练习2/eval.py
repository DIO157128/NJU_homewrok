import os
import random

import torch
from torchvision.models.detection import fasterrcnn_resnet50_fpn
from torchvision.transforms import functional as F
from PIL import Image
# 加载训练好的模型
model = fasterrcnn_resnet50_fpn(pretrained=False)  # 不使用预训练权重
model.load_state_dict(torch.load('best_model.pth'))
model.eval()

# 输入图像文件夹路径
image_folder = "练习2数据集/JPEGImages/test"

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
            output_num = random.randint(1,2)
            count = 0
            for score, label, box in zip(predictions["scores"], predictions["labels"], predictions["boxes"]):
                box = box.tolist()  # 将Tensor转换为列表
                line = f"oil {score:.4f} {box[0]:.2f} {box[1]:.2f} {box[2]:.2f} {box[3]:.2f}\n"
                file.write(line)
                count+=1
                if count==output_num:
                    break