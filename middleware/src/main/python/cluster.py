# -*- coding:utf-8 -*-
# @author  : shenfei
# @time    : 2025/4/5 22:10
# @function: 构建&刷新 学生&教师 数字画像
import ast
import json

import pandas as pd
from fastapi import APIRouter, Body
from joblib import dump, load
from sklearn.cluster import DBSCAN
from sklearn.cluster import KMeans
from sklearn.preprocessing import LabelEncoder
import numpy as np
import category_encoders as ce

cluster = APIRouter()


# 自定义序列化函数
def convert_numpy(obj):
    if isinstance(obj, (np.int32, np.int64)):
        return int(obj)
    elif isinstance(obj, (np.float32, np.float64)):
        return float(obj)
    elif isinstance(obj, np.ndarray):
        return obj.tolist()
    return obj

@cluster.post('/buildStuCluster', description='训练学生聚类器')
def buildStuCluster(stu_wide_table: list = Body(..., embed=True), target_columns: list[str] = Body(..., embed=True),
                    algo: dict = Body(..., embed=True)):
    try:
        configuration = algo[algo['profileAlgorithm']]  # 加载参数
        kmeans_cluster = KMeans(n_clusters=configuration['nClusters'], init=configuration['init'],
                         max_iter=configuration['maxIter'],
                         n_init=configuration['nInit'], tol=configuration['tol'],
                         random_state=configuration['randomState'],
                         copy_x=configuration['copyX'])

        # 获取所有可能的列名
        all_columns = set()
        for row in stu_wide_table:
            all_columns.update(row.keys())

        # 填充缺失的键为 NaN
        stu_wide_table_filled = []
        for row in stu_wide_table:
            filled_row = {col: row.get(col, np.nan) for col in all_columns}
            stu_wide_table_filled.append(filled_row)

        # 读取训练数据
        df = pd.DataFrame(stu_wide_table_filled)

        # 检查数据是否为空
        if df.empty:
            return {'error': '输入数据为空'}

        # 保留传入的筛选列
        selected_keys = [key for key in df.columns if key in target_columns]
        if not selected_keys:
            return {'error': '没有找到匹配的目标列'}

        temp_table = df[selected_keys]

        # 删除含缺失值的行
        temp_table = temp_table.dropna()

        # 检查处理后是否还有数据
        if temp_table.empty:
            return {'error': '删除缺失值后数据为空'}

        # 检查数据量是否足够聚类
        if len(temp_table) < configuration['nClusters']:
            return {'error': f'数据量({len(temp_table)})少于聚类数({configuration["nClusters"]}), 无法进行聚类'}

        # 处理类别特征和数值特征
        cat_cols = temp_table.select_dtypes(include=['object']).columns.tolist()
        num_cols = temp_table.select_dtypes(exclude=['object']).columns.tolist()

        # 如果有类别特征，进行编码
        if cat_cols:
            encoder = ce.HashingEncoder(cols=cat_cols, n_components=8)
            temp_table_encoded = encoder.fit_transform(temp_table[cat_cols])
            # 确保编码后的数据是数值型
            temp_table_encoded = temp_table_encoded.astype(float)
        else:
            temp_table_encoded = pd.DataFrame()

        # 合并数值型与编码后的类别特征
        if num_cols:
            num_data = temp_table[num_cols].astype(float)
            if not temp_table_encoded.empty:
                temp_table_processed = pd.concat([num_data, temp_table_encoded], axis=1)
            else:
                temp_table_processed = num_data
        else:
            if not temp_table_encoded.empty:
                temp_table_processed = temp_table_encoded
            else:
                return {'error': '没有可用于聚类的特征'}

        # 处理无穷大和NaN值
        temp_table_processed = temp_table_processed.replace([np.inf, -np.inf], np.nan)
        temp_table_processed = temp_table_processed.fillna(0)

        # 确保所有数据都是有限的数值
        if not np.isfinite(temp_table_processed.values).all():
            return {'error': '数据包含无效值(无穷大或NaN)'}

        # 进行聚类
        kmeans_cluster.fit(temp_table_processed)
        result = kmeans_cluster.predict(temp_table_processed)

        # 将聚类结果合并到原始数据
        result_list = result.tolist()
        for i in range(len(stu_wide_table)):
            if i < len(result_list):
                stu_wide_table[i]['clusterResult'] = result_list[i]
            else:
                stu_wide_table[i]['clusterResult'] = -1  # 标记为未聚类

            # 格式化日期字符串
            if 'registerDate' in stu_wide_table[i]:
                date = stu_wide_table[i]['registerDate']
                if isinstance(date, list) and len(date) >= 3:
                    try:
                        formatted_date = f"{date[0]}-{date[1]:02d}-{date[2]:02d}"
                        stu_wide_table[i]['registerDate'] = formatted_date
                    except (TypeError, ValueError):
                        pass  # 保持原始格式

        return {'data': json.dumps(stu_wide_table, default=convert_numpy, ensure_ascii=False)}

    except Exception as e:
        return {'error': f'聚类过程中发生错误: {str(e)}'}


@cluster.post('/buildTeacherCluster', description='训练教师聚类器')
def buildTeacherCluster(teacher_wide_table: list = Body(..., embed=True), target_columns: list[str] = Body(..., embed=True),
                        algo: dict = Body(..., embed=True)):
    try:
        configuration = algo[algo['profileAlgorithm']]  # 加载参数
        kmeans_cluster = KMeans(n_clusters=configuration['nClusters'], init=configuration['init'],
                         max_iter=configuration['maxIter'],
                         n_init=configuration['nInit'], tol=configuration['tol'],
                         random_state=configuration['randomState'],
                         copy_x=configuration['copyX'])

        # 获取所有可能的列名
        all_columns = set()
        for row in teacher_wide_table:
            all_columns.update(row.keys())

        # 填充缺失的键为 NaN
        teacher_wide_table_filled = []
        for row in teacher_wide_table:
            filled_row = {col: row.get(col, np.nan) for col in all_columns}
            teacher_wide_table_filled.append(filled_row)

        # 读取训练数据
        df = pd.DataFrame(teacher_wide_table_filled)

        # 检查数据是否为空
        if df.empty:
            return {'error': '输入数据为空'}

        # 保留传入的筛选列
        available_columns = [col for col in target_columns if col in df.columns]
        if not available_columns:
            return {'error': '没有找到匹配的目标列'}

        temp_table = df[available_columns]

        # 删除含缺失值的行
        temp_table = temp_table.dropna()

        # 检查处理后是否还有数据
        if temp_table.empty:
            return {'error': '删除缺失值后数据为空'}

        # 检查数据量是否足够聚类
        if len(temp_table) < configuration['nClusters']:
            return {'error': f'数据量({len(temp_table)})少于聚类数({configuration["nClusters"]}), 无法进行聚类'}

        # 处理类别特征和数值特征
        cat_cols = temp_table.select_dtypes(include=['object']).columns.tolist()
        num_cols = temp_table.select_dtypes(exclude=['object']).columns.tolist()

        # 如果有类别特征，进行编码
        if cat_cols:
            encoder = ce.HashingEncoder(cols=cat_cols, n_components=8)
            temp_table_encoded = encoder.fit_transform(temp_table[cat_cols])
            # 确保编码后的数据是数值型
            temp_table_encoded = temp_table_encoded.astype(float)
        else:
            temp_table_encoded = pd.DataFrame()

        # 合并数值型与编码后的类别特征
        if num_cols:
            num_data = temp_table[num_cols].astype(float)
            if not temp_table_encoded.empty:
                temp_table_processed = pd.concat([num_data, temp_table_encoded], axis=1)
            else:
                temp_table_processed = num_data
        else:
            if not temp_table_encoded.empty:
                temp_table_processed = temp_table_encoded
            else:
                return {'error': '没有可用于聚类的特征'}

        # 处理无穷大和NaN值
        temp_table_processed = temp_table_processed.replace([np.inf, -np.inf], np.nan)
        temp_table_processed = temp_table_processed.fillna(0)

        # 确保所有数据都是有限的数值
        if not np.isfinite(temp_table_processed.values).all():
            return {'error': '数据包含无效值(无穷大或NaN)'}

        # 进行聚类
        kmeans_cluster.fit(temp_table_processed)
        result = kmeans_cluster.predict(temp_table_processed)

        # 将聚类结果合并到原始数据
        result_list = result.tolist()
        for i in range(len(teacher_wide_table)):
            if i < len(result_list):
                teacher_wide_table[i]['clusterResult'] = result_list[i]
            else:
                teacher_wide_table[i]['clusterResult'] = -1  # 标记为未聚类

            # 格式化日期字符串
            if 'entryDate' in teacher_wide_table[i]:
                date = teacher_wide_table[i]['entryDate']
                if isinstance(date, list) and len(date) >= 3:
                    try:
                        formatted_date = f"{date[0]}-{date[1]:02d}-{date[2]:02d}"
                        teacher_wide_table[i]['entryDate'] = formatted_date
                    except (TypeError, ValueError):
                        pass  # 保持原始格式

        return {'data': json.dumps(teacher_wide_table, default=convert_numpy, ensure_ascii=False)}

    except Exception as e:
        return {'error': f'聚类过程中发生错误: {str(e)}'}