import pandas as pd
from math import *
import numpy as np
from tqdm import tqdm

def readcsv(movie_path , rating_path ,save_path):
    movies = pd.read_csv(movie_path)
    ratings = pd.read_csv(rating_path)
    data = pd.merge(movies,ratings,on="movieId")
    data = data[['userId', 'rating', 'movieId', 'title']].sort_values('userId')
    data.to_csv(save_path,index=False)

def createUserDict(path):
    file = open(path, 'r', encoding='UTF-8')
    data = {}
    count=0
    for line in file.readlines():
        if count == 0 :
            count += 1
            continue
        line = line.strip().split(',')
        line[0]=int(line[0])
        line[1] = float(line[1])
        line[2] = int(line[2])
        if not line[0] in data.keys():
            data[line[0]] = {line[2]: line[1]}
        else:
            data[line[0]][line[2]] = line[1]

    return data

# 计算两位用户的相对距离（欧氏距离）
def Euclidean(user1, user2,data):
    user1_data = data[user1]
    user2_data = data[user2]
    distance = 0
    for key in user1_data.keys():
        if key in user2_data.keys():
            distance += pow(float(user1_data[key]) - float(user2_data[key]), 2)

    return 1 / (1 + sqrt(distance))

# 为客户匹配与他口味最相似的其他10个客户

def top_simliar_users(userid_to_cal,data):
    res = []
    for userid in data.keys():
        # 排除与自己计算相似度
        userid=int(userid)
        if not userid == userid_to_cal:
            similarity = Euclidean(userid_to_cal, userid, data)
            res.append((userid, similarity))
    res.sort(key=lambda x: x[1])
    return res[:10]

# 给定用户id，查找与该用户相似度最高的用户评分，降序排列前5部电影
def recommend(user,data):
    top_sim_users = top_simliar_users(user, data)
    sim_all_neighbors = np.sum(top_sim_users,axis=0)[1]
    recommendations = {}
    for top_sim_user in top_sim_users:
        movies = data[top_sim_user[0]]
        sim_current_user = top_sim_user[1]
        for movie in movies.keys():
            rating = movies[movie]
            if movie not in data[user].keys():
                if movie not in recommendations.keys():
                    recommendations[movie] = rating * sim_current_user / sim_all_neighbors
                else:
                    recommendations[movie] += rating * sim_current_user / sim_all_neighbors
    recommendations = sorted(recommendations.items(), key = lambda x:-x[1])

    return recommendations[:5]

def main(numofusers,movie_path , rating_path ,save_path):
    print("start reading data and merging!")
    readcsv(movie_path , rating_path ,save_path)
    print("reading success! start transforming data into dict")
    userdata = createUserDict(save_path)
    print("transforming success! start recommending")
    res = pd.DataFrame(columns=('USERID', 'MOVIEID'))
    for userid in tqdm(range(1,numofusers+1)):
        recommendations = recommend(userid,userdata)
        for rec in recommendations:
            res = res.append([{'USERID': userid,"MOVIEID":rec[0]}], ignore_index=True)
    res.to_csv("movie.csv",index=False)

if __name__ =="__main__":
    main(610 , "./ml-latest-small/movies.csv", "./ml-latest-small/ratings.csv","./ml-latest-small/mergedata.csv")

