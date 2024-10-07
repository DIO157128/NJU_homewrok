# Easy data augmentation techniques for text classification
# Jason Wei and Kai Zou
import random

import pandas as pd
import tqdm

import SentiCR.sentiCR


#the output file
from SentiCR.eda_process.code import eda, eda_old

output = None

#number of augmented sentences to generate per original sentence
num_aug = 2 #default

#how much to replace each word by synonyms
alpha_sr = 0.1#default

#how much to insert new words that are synonyms
alpha_ri = 0.1#default

#how much to swap words
alpha_rs = 0.1#default

#how much to delete words
alpha_rd = 0.1#default

#generate more data with standard augmentation
def gen_eda(oracle_data, alpha_sr, alpha_ri, alpha_rs, alpha_rd, num_aug=9):
    res_rating = []
    res_sentence = []
    count = 0
    for sentidata in tqdm.tqdm(oracle_data):
        count = count + 1
        rating = sentidata.rating
        sentence = SentiCR.sentiCR.da_preprocess_text(sentidata.text)

        aug_sentences = eda.eda_process(sentence, alpha_sr=alpha_sr, alpha_ri=alpha_ri, alpha_rs=alpha_rs, p_rd=alpha_rd, num_aug=num_aug)
        for s in aug_sentences:
            if s != "" and s:
                res_rating.append(rating)
                res_sentence.append(s.lower())
    zipped = list(zip(res_sentence, res_rating))
    random.shuffle(zipped)
    list1_shuffled, list2_shuffled = zip(*zipped)
    df = pd.DataFrame({'col1':list1_shuffled,'col2':list2_shuffled})
    df.to_excel('output_4.xlsx',index=False,header=False)

def start_eda(oracle_data):
    return gen_eda(oracle_data,alpha_sr,alpha_ri,alpha_rs,alpha_rd,num_aug)