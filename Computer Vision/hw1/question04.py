import cv2
import numpy as np
import tqdm


def replace_pixel_values(image1_path, image2_path,channel):
    # 读取I1和I2的灰度图像
    image1 = cv2.imread(image1_path)
    image2 = cv2.imread(image2_path, cv2.IMREAD_GRAYSCALE)
    b, g, r = cv2.split(image1)
    channel_pixels = eval(channel)
    # 获取图像宽度和高度
    height, width = channel_pixels.shape
    result_images = []
    for k in range(8):
        binary_value = 1 << k
        # 创建结果图像，与输入图像相同大小
        result_image = np.zeros((height, width), dtype=np.uint8)

        # 遍历图像的每个像素
        for i in tqdm.tqdm(range(height)):
            for j in range(width):
                # 获取I2中当前像素的灰度值
                pixel_value = image2[i, j]

                # 获取I1中当前像素的灰度值
                replacement_value = channel_pixels[i, j]
                # 检查I2当前像素的灰度值中的该位是否为1
                if pixel_value & binary_value:
                    # 将I1对应像素的灰度值中的该位设置为1
                    replacement_value |= binary_value
                else:
                    # 将I1对应像素的灰度值中的该位设置为0
                    replacement_value &= ~binary_value

                # 将替换后的灰度值赋给结果图像的当前像素
                result_image[i, j] = replacement_value
        if channel == 'b':
            result_image = cv2.merge((result_image, g, r))
        elif channel == 'g':
            result_image = cv2.merge((b, result_image, r))
        elif channel == 'r':
            result_image = cv2.merge((b, g, result_image))
        else:
            raise ValueError("Invalid channel. Please use 'b', 'g', or 'r'.")
        result_images.append(result_image)
    return result_images

# 指定I1和I2的灰度图像路径
image1_path = 'hw01-I0.jpeg'
image2_path = 'hw01-I2.jpeg'

# 执行像素值替换
result_images = replace_pixel_values(image1_path, image2_path,'r')

for i in range(8):
    # 保存结果图像
    cv2.imwrite('question4/hw01-I0-{}.jpeg'.format(i), result_images[i])

