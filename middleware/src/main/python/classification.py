import ast
import json
from datetime import datetime

import pandas as pd
from fastapi import Body, APIRouter
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier, plot_tree, _tree
from sklearn.metrics import mean_squared_error, r2_score
from sklearn.preprocessing import OneHotEncoder, LabelEncoder
import joblib
import numpy as np
import random
import category_encoders as ce
import os


classification = APIRouter()


@classification.post('/decisionTreeClassifyTrain', description='训练预警模型')
def DecisionTreeClassifyTrain(stu_wide_table: list = Body(..., embed=True),
                              target_columns: list[str] = Body(..., embed=True),
                              algo: dict = Body(..., embed=True)):
    # 读取训练数据
    df = pd.DataFrame(stu_wide_table)

    # 删除含缺失值的行（根据实际情况选择填充或删除）
    df.dropna(inplace=True)

    X = df.drop('flag', axis=1)  # 特征矩阵
    y = df['flag']  # 目标变量

    # 保留传入的筛选列
    selected_keys = [key for key in df.columns if key in target_columns]
    X = df[selected_keys]


    # 删除含缺失值的行（根据实际情况选择填充或删除）
    df.dropna(inplace=True)

    # 类别特征编码（自动识别非数值型特征）
    cat_cols = X.select_dtypes(include=['object']).columns
    encoder = ce.HashingEncoder(cols=[str(col) for col in cat_cols], n_components=8)
    X_encoded = pd.DataFrame(encoder.fit_transform(X[cat_cols]))
    X_encoded.columns = encoder.get_feature_names_out(cat_cols)

    # 合并数值型与编码后的类别特征
    num_cols = X.select_dtypes(exclude=['object']).columns
    X_processed = pd.concat([X[num_cols], X_encoded], axis=1)

    # 随机拆分：50%训练，50%测试，固定随机种子保证可复现性
    # X_train, X_test, y_train, y_test = train_test_split(
    #     X_processed, y,
    #     test_size=0.2,
    #     random_state=42
    # )
    X_train = X_processed
    y_train = y

    # === 模型训练与预测 ===
    # 设置ID3参数（criterion='entropy'）[8,9](@ref)
    model = DecisionTreeClassifier(
        criterion='entropy',  # 信息增益准则
        random_state=42,  # 确保结果可复现
        min_samples_leaf=10,  # 防止过拟合[5](@ref)
        class_weight='balanced', # 平衡正负样本
        max_depth=5, # 限制最大深度,防止过拟合
        min_weight_fraction_leaf=0.01 # 剪枝
    )

    # 数据训练
    model.fit(X_train, y_train)

    # === 模型保存 ===
    model_name = datetime.now().strftime("%Y%m%d_%H%M%S")
    folder = "./out"
    if not os.path.exists(folder):
        os.makedirs(folder)
    filename = model_name + '.pkl'
    save_path = os.path.join(folder, filename)
    joblib.dump({'encoder': encoder, 'model': model, 'attributes': target_columns}, save_path)
    return {'data': model_name}


@classification.post('/decisionTreeClassifyPredict', description='预警模型预测')
def DecisionTreeClassifyPredict(stu_wide_table: list = Body(..., embed=True), model_name: str = Body(..., embed=True)):

    folder = "out"
    filename = model_name + '.pkl'
    save_path = os.path.join(folder, filename)

    # 加载编码器,模型,筛选列
    saved_data = joblib.load(save_path)
    encoder = saved_data['encoder']
    model = saved_data['model']
    attributes = saved_data['attributes']

    # 模拟读取特定辅导员名下的学生
    df = pd.DataFrame(stu_wide_table)

    # 删除含缺失值的行（根据实际情况选择填充或删除）
    # df.dropna(inplace=True)

    # 保留传入的列
    X = df[[col for col in df.columns if col in attributes]]

    # 对新数据编码（使用transform而非fit_transform!）
    # 类别特征编码（自动识别非数值型特征）
    cat_cols = X.select_dtypes(include=['object']).columns
    X_encoded = pd.DataFrame(encoder.transform(X[cat_cols]))
    X_encoded.columns = encoder.get_feature_names_out(cat_cols)

    # 合并数值型与编码后的类别特征
    num_cols = X.select_dtypes(exclude=['object']).columns
    X_processed = pd.concat([X[num_cols], X_encoded], axis=1)

    # 删除含缺失值的行（根据实际情况选择填充或删除）
    X_processed = X_processed.fillna(0)

    # 预警分类预测
    y_pred = model.predict(X_processed)
    # 获取置信度
    confidence_scores = model.predict_proba(X_processed)[:, 1]

    # === 结果保存 ===
    result_df = df.copy()
    result_df['confidence'] = confidence_scores.round(3)  # 保留3位小数

    # result_df.to_excel("check.xlsx",index=True)

    # === 格式化日期字符串 ===
    result = result_df.to_dict('records')
    for i in range(len(result)):
        date = result[i]['registerDate']
        # date_parts = ast.literal_eval(date)  # 输出: [2019, 9, 1]
        date_parts = date  # 输出: [2019, 9, 1]
        formatted_date = f"{date_parts[0]}-{date_parts[1]:02d}-{date_parts[2]:02d}"
        result[i]['registerDate'] = formatted_date
    return {'data': json.dumps(result, default=convert_numpy, ensure_ascii=False)}


def get_feature_contributions(model, X_sample):
    """
    获取单个样本的决策路径特征贡献度
    :param model: 训练好的DecisionTreeClassifier
    :param X_sample: 单个样本的特征数据（1D数组）
    :return: 各特征贡献度字典（键为特征名，值为贡献度分数）
    """
    # 获取决策树结构和特征名
    tree_ = model.tree_
    feature_names = model.feature_names_in_

    # 获取样本的决策路径节点列表
    node_indices = tree_.decision_path(X_sample.reshape(1, -1)).indices

    # 计算贡献度（按节点深度加权）
    contributions = {}
    for node_id in node_indices:
        if tree_.feature[node_id] != _tree.TREE_UNDEFINED:  # 非叶节点
            feature = feature_names[tree_.feature[node_id]]
            depth = tree_.n_node_samples[node_id]  # 或用 tree_.depth[node_id]
            contributions[feature] = contributions.get(feature, 0) + depth

    # 归一化贡献度（总和为1）
    total = sum(contributions.values())
    return {k: v / total for k, v in contributions.items()}


# 自定义序列化函数
def convert_numpy(obj):
    if isinstance(obj, (np.int32, np.int64)):
        return int(obj)
    elif isinstance(obj, (np.float32, np.float64)):
        return float(obj)
    elif isinstance(obj, np.ndarray):
        return obj.tolist()
    return obj