# -*- coding:utf-8 -*-
# @author  : shenfei
# @time    : 2025/4/7 18:13
# @function: the script is used to do something.

import pandas as pd
from fastapi import APIRouter, Body
from pydantic import BaseModel
from sklearn.preprocessing import MinMaxScaler
import numpy as np

summarize = APIRouter()


class SummarizeResponse(BaseModel):
    data: list


def get_weight(df, columns):
    for col in columns:
        # 计算每个值的频率（出现次数 / 总行数）
        freq = df[col].value_counts(normalize=True)
        # 映射频率到原列，计算新值
        df[col] = (1 - df[col].map(freq)) * df[col]


@summarize.post('/getScores', description='负责给出宽表中的健康指数')
async def getScores(stu_wide_table: list = Body(..., embed=True)):
    df = pd.DataFrame(stu_wide_table)
    target_study_columns = ['individualAwardCount', 'latestScholarshipLevel', 'prizeCount',
                            'competitionCount', 'paperCount', 'eiPaperCount', 'sciPaperCount']
    # highestPunishmentLevel需要特殊处理
    target_grad_columns = ['thesisProposal', 'middleCheck', 'duplicateCheck', 'blindReview', 'preDefense',
                           'defense']
    target_work_columns = ['jobIntention', 'sign', 'offer', 'admission']
    target_living_columns = ['stdFee', 'stdFoodFee', 'averageFoodCount', 'stdFoodCount', 'stdBathFee',
                             'stdBathCount', 'stdStoreFee', 'stdStoreCount', 'stdStadiumCount',
                             'stdExitCount', 'stdStayHour', 'stdInternetHour', 'stdMobileHour',
                             'stdPcHour', 'stdForumPost', 'stdForumReply', 'stdLateNightPost',
                             'stdLateNightReply']

    raw_df = df.copy()
    df['highestPunishmentLevel'] = df['highestPunishmentLevel'].map(
        {'无': 1, '处分': 1 / 8, '通报批评': 1 / 4, '留校查看': 1 / 2})
    df['latestScholarshipLevel'] = df['latestScholarshipLevel'].map({'二等奖学金': 1, '一等奖学金': 2})

    # 这里使用频率作为权重，即频率越高的正向指标，代表越容易获得，所以权重相对较低
    get_weight(df, target_study_columns)
    # 这里特殊处理了惩罚等级，因为惩罚制度相对来说严重性和优先级更高，所以选择直接乘的方式，即受到处分的话，立马失去一半的分数
    df['AcademicIndex'] = df[target_study_columns].sum(axis=1) * df['highestPunishmentLevel']

    # 最后进行归一化操作，使得分数稳定在【0,1】中
    temp_min = df['AcademicIndex'].min()
    temp_max = df['AcademicIndex'].max()
    raw_df['academicIndex'] = (df['AcademicIndex'] - temp_min) / (temp_max - temp_min)

    # 设置毕业的每一个考核，如果暂未涉及即不影响，如果通过，则进度加一，如果有一处为通过，则减掉一个局部极大值
    # 此时毕设指数肯定会变成0，即不安全
    df['middleCheck'] = df['middleCheck'].map({'通过': 1, '暂未涉及': 0, '未通过': -6})
    df['thesisProposal'] = df['thesisProposal'].map({'通过': 1, '延期': -6, '暂未涉及': 0})
    df['duplicateCheck'] = df['duplicateCheck'].map({'通过': 1, '未通过': -6, '暂未涉及': 0})
    df['blindReview'] = df['blindReview'].map({'通过': 1, '未通过': -6, '暂未涉及': 0})
    df['preDefense'] = df['preDefense'].map({'通过': 1, '未通过': -6, '暂未涉及': 0})
    df['defense'] = df['defense'].map({'通过': 1, '未通过': -6, '暂未涉及': 0})

    df['GraduationDesignIndex'] = df[target_grad_columns].sum(axis=1) / 6
    raw_df['graduationDesignIndex'] = df['GraduationDesignIndex'].apply(lambda x: x if x > 0 else 0)

    df['jobIntention'] = df['jobIntention'].apply(int)
    # 针对意向，offer，签约等流程，奖励都设置为1，不需要考虑减去极大值的情况
    # 因为这四个步骤都不是就业指数的必要条件
    df['sign'] = df['sign'].map({True: 1, False: 0})
    df['offer'] = df['offer'].map({True: 1, False: 0})
    df['admission'] = df['admission'].map({True: 1, False: 0})
    raw_df['employmentIndex'] = df[target_work_columns].sum(axis=1) / 4

    # 下方对标准差数据进行无量纲化，便于后面熵权法
    # 这里增加极小值偏移，否则会出现被除数是0的情况
    scaler = MinMaxScaler(feature_range=(0.00001, 1))
    temp_data=df[target_living_columns].to_numpy()
    x_std=pd.DataFrame(scaler.fit_transform(temp_data))
    df_normalized=pd.DataFrame(x_std,columns=target_living_columns)

    # 给出样本数量与熵系数
    n=len(df_normalized)
    k=1/np.log(n)

    entropies=[]
    for col in target_living_columns:
        p=df_normalized[col]/df_normalized[col].sum()
        entropy=-k*(p*np.log(p)).sum()
        entropies.append(entropy)

    # 计算信息量，即权重
    diversities=1-np.array(entropies)
    weights=diversities/diversities.sum()

    weights_series=pd.Series(weights,index=target_living_columns)
    df['LivingIndex']=df_normalized.dot(weights_series)

    # df['LivingIndex'] = df[target_living_columns].sum(axis=1)
    # temp_min = df['LivingIndex'].min()
    # temp_max = df['LivingIndex'].max()
    # raw_df['livingIndex'] = (df['LivingIndex'] - temp_min) / (temp_max - temp_min)

    raw_df['registerDate'] = df['registerDate'].apply(lambda x: f"{x[0]}-{x[1]:02d}-{x[2]:02d}")
    
    return {"data": raw_df.to_json(orient='records', force_ascii=False)}
