import cv2


def convert_to_grayscale(image_path):
    # 读取彩色图像
    image = cv2.imread(image_path)

    # 转换为灰度图像
    gray_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    return gray_image


# 指定彩色图像路径
color_image_path = 'hw01-I0.jpeg'

# 转换为灰度图像
gray_image = convert_to_grayscale(color_image_path)

# 保存灰度图像
cv2.imwrite('hw01-I1.jpeg', gray_image)
