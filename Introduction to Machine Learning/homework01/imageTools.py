import numpy as np
import PIL.Image as image
def load_data(file_path):
    '''导入数据
    input:  file_path(string):文件的存储位置
    output: data(mat):数据
    '''
    f = open(file_path, "rb")  # 以二进制的方式打开图像文件
    data = []
    im = image.open(f)  # 导入图片
    m, n = im.size  # 得到图片的大小
    print(m, n)
    for i in range(m):
        for j in range(n):
            tmp = []
            x, y, z = im.getpixel((i, j))
            tmp.append(x / 256.0)
            tmp.append(y / 256.0)
            tmp.append(z / 256.0)
            data.append(tmp)
    f.close()
    return np.mat(data)
def save_pic(filename,k):
    center_path = filename+"_{}_center_pp".format(k)
    f_center = open(center_path)
    center = []
    for line in f_center.readlines():
        lines = line.strip().split("\t")
        tmp = []
        for x in lines:
            tmp.append(int(float(x) * 256))
        center.append(tuple(tmp))
    f_center.close()
    fp = open(filename+".jpg", "rb")
    im = image.open(fp)
    # 新建一个图片
    m, n = im.size
    pic_new = image.new("RGB", (m, n))
    sub_path = filename+"_{}_sub_pp".format(k)
    f_sub = open(sub_path)
    i = 0
    for line in f_sub.readlines():
        index = float((line.strip().split("\t"))[0])
        index_n = int(index)
        pic_new.putpixel(((int)(i / n), (i % n)), center[index_n])
        i = i + 1
    f_sub.close()
    pic_new.save("result_{}_{}.jpg".format(k,filename), "JPEG")
