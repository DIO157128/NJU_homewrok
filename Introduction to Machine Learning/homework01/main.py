import numpy as np
import PIL.Image as image

from imageTools import load_data, save_pic
from kmeans import getKmeansRes

if __name__=="__main__":
    path = "test.jpg"
    filename= "test"
    k = 2
    data = load_data(path)
    getKmeansRes(k,data,filename)
    save_pic(filename,k)