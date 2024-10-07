import cv2


def convert_to_binary(image_path, width, height):
    # 读取图像
    image = cv2.imread(image_path)

    # 调整图像大小
    resized_image = cv2.resize(image, (width, height))

    # 将图像转换为灰度图像
    gray_image = cv2.cvtColor(resized_image, cv2.COLOR_BGR2GRAY)

    # 对灰度图像进行二值化处理
    _, binary_image = cv2.threshold(gray_image, 127, 255, cv2.THRESH_BINARY)

    return binary_image


# 指定原始图像路径
image_path = 'ilovenju.jpg'

# 指定目标分辨率
target_width = 5922
target_height = 3567

# 转换为二值图像
binary_image = convert_to_binary(image_path, target_width, target_height)

# 保存二值图像
cv2.imwrite('hw01-I2.jpeg', binary_image)
