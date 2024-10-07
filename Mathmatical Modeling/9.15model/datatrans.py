import numpy as np
import pandas as pd

class relics:
    def __init__(self, no, ornament , type , color , weather):
        self.no = no
        self.ornament = ornament
        self.type = type
        self.color = color
        self.weather = weather

def readData1(path):
    f = open("map.txt",'w')
    df = pd.read_csv(path)
    ornament = np.array(df["纹饰"]).tolist()
    type = np.array(df["类型"]).tolist()
    color = np.array(df["颜色"]).tolist()
    weather = np.array(df["表面风化"]).tolist()
    m_ornament = {}
    f.write("ornament:\n")
    for i in range(len(set(ornament))):
        m_ornament[list(set(ornament))[i]]=i+1
        f.write("{}:{}\n".format(list(set(ornament))[i],i+1))
    m_type = {}
    f.write("type:\n")
    for i in range(len(set(type))):
        m_type[list(set(type))[i]]=i+1
        f.write("{}:{}\n".format(list(set(type))[i],i+1))
    m_color = {}
    f.write("color:\n")
    for i in range(len(set(color))):
        m_color[list(set(color))[i]]=i+1
        f.write("{}:{}\n".format(list(set(color))[i],i+1))
    m_weather = {}
    f.write("weather:\n")
    for i in range(len(set(weather))):
        m_weather[list(set(weather))[i]]=i+1
        f.write("{}:{}\n".format(list(set(weather))[i],i+1))
    no_dealed = []
    ornament_dealed = []
    type_dealed = []
    color_dealed = []
    weather_dealed = []
    for i in range(len(ornament)):
        no_dealed.append(i+1)
        ornament_dealed.append(m_ornament[ornament[i]])
        type_dealed.append(m_type[type[i]])
        color_dealed.append(m_color[color[i]])
        weather_dealed.append(m_weather[weather[i]])
    df_new = pd.DataFrame()
    df_new["no"] = no_dealed
    df_new["ornament"] = ornament_dealed
    df_new["type"] = type_dealed
    df_new["color"] = color_dealed
    df_new["weather"] = weather_dealed
    df_new.to_csv("sheet1_dealt.csv")
if __name__ == "__main__":
    readData1("sheet1/sheet1.csv")