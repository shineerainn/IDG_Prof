import json
import numpy as np
import pandas as pd
from fastapi import APIRouter, Body
from sklearn.cluster import KMeans, DBSCAN, AgglomerativeClustering
from sklearn.preprocessing import StandardScaler, MinMaxScaler
from sklearn.decomposition import PCA
from sklearn.metrics import silhouette_score, calinski_harabasz_score
import matplotlib.pyplot as plt
import seaborn as sns
from typing import List, Dict, Any, Optional
import warnings
warnings.filterwarnings('ignore')

academic_cluster = APIRouter()

# 自定义序列化函数
def convert_numpy(obj):
    if isinstance(obj, (np.int32, np.int64)):
        return int(obj)
    elif isinstance(obj, (np.float32, np.float64)):
        return float(obj)
    elif isinstance(obj, np.ndarray):
        return obj.tolist()
    return obj

class AcademicClusterAnalyzer:
    """学业特征聚类分析器"""
    
    def __init__(self):
        self.scaler = StandardScaler()
        self.pca = PCA(n_components=2)
        self.cluster_model = None
        self.feature_names = [
            # 学业表现相关
            'weightedAvgGrade', 'coreCourseGrade', 'lastSemesterAvgGrade',
            'gpaGrowthRate', 'gradeRanking', 'rankingLevel', 'academicPerformanceScore',
            
            # 科研项目相关
            'researchProjectLevel', 'projectRole', 'projectAchievementTransformation', 'researchProjectScore',
            
            # 论文相关
            'thesisDefenseGrade', 'thesisInnovativeness', 'thesisPracticalValue',
            'thesisAchievementTransformation', 'thesisScore',
            
            # 学术论文相关
            'publishedJournalConference', 'authorLevel', 'isHighlyCited', 'isIfAbove',
            'isCoverPaper', 'academicPaperScore',
            
            # 竞赛相关
            'competitionLevel', 'awardLevel', 'competitionScope', 'competitionTeamRole', 'competitionScore',
            
            # 专利软件相关
            'patentSoftwareType', 'legalStatusCoefficient', 'technologyTransferAmount', 'patentSoftwareScore',
            
            # 其他创新成果相关
            'hasStartupProject', 'startupFundingStage', 'isCompanyRegistered', 'hasStartupCompetitionAward',
            'techApplicationEnterpriseCount', 'techPromotionContractAmount', 'otherInnovationAchievementScore'
        ]
        
        # 默认特征权重（可根据培养阶段动态调整）
        self.default_weights = {
            # 学业表现相关权重
            'weightedAvgGrade': 0.08, 'coreCourseGrade': 0.06, 'lastSemesterAvgGrade': 0.05,
            'gpaGrowthRate': 0.03, 'gradeRanking': 0.02, 'rankingLevel': 0.01, 'academicPerformanceScore': 0.10,
            
            # 科研项目相关权重
            'researchProjectLevel': 0.05, 'projectRole': 0.03, 'projectAchievementTransformation': 0.02, 'researchProjectScore': 0.10,
            
            # 论文相关权重
            'thesisDefenseGrade': 0.05, 'thesisInnovativeness': 0.04, 'thesisPracticalValue': 0.03,
            'thesisAchievementTransformation': 0.03, 'thesisScore': 0.10,
            
            # 学术论文相关权重
            'publishedJournalConference': 0.03, 'authorLevel': 0.02, 'isHighlyCited': 0.02, 'isIfAbove': 0.02,
            'isCoverPaper': 0.01, 'academicPaperScore': 0.10,
            
            # 竞赛相关权重
            'competitionLevel': 0.03, 'awardLevel': 0.02, 'competitionScope': 0.02, 'competitionTeamRole': 0.01, 'competitionScore': 0.07,
            
            # 专利软件相关权重
            'patentSoftwareType': 0.02, 'legalStatusCoefficient': 0.01, 'technologyTransferAmount': 0.01, 'patentSoftwareScore': 0.06,
            
            # 其他创新成果相关权重
            'hasStartupProject': 0.01, 'startupFundingStage': 0.01, 'isCompanyRegistered': 0.01, 'hasStartupCompetitionAward': 0.01,
            'techApplicationEnterpriseCount': 0.01, 'techPromotionContractAmount': 0.01, 'otherInnovationAchievementScore': 0.05
        }
    
    def prepare_data(self, academic_data: List[Dict], weights: Optional[Dict] = None) -> pd.DataFrame:
        """准备聚类数据"""
        # 转换为DataFrame
        df = pd.DataFrame(academic_data)
        
        # 检查必要的特征列是否存在
        missing_features = [f for f in self.feature_names if f not in df.columns]
        if missing_features:
            print(f"警告：缺少特征列: {missing_features}")
            # 为缺失的特征列添加默认值
            for feature in missing_features:
                df[feature] = 0.0
        
        # 提取特征列
        feature_data = df[self.feature_names].copy()
        
        # 处理缺失值和异常值
        feature_data = feature_data.fillna(0)
        
        # 确保所有数据都是数值类型
        for col in feature_data.columns:
            feature_data[col] = pd.to_numeric(feature_data[col], errors='coerce').fillna(0)
        
        # 应用权重（如果提供）
        if weights:
            for feature, weight in weights.items():
                if feature in feature_data.columns:
                    feature_data[feature] = feature_data[feature] * weight
        
        print(f"准备的数据形状: {feature_data.shape}")
        print(f"数据预览:\n{feature_data.head()}")
        
        return feature_data
    
    def perform_clustering(self, data: pd.DataFrame, algorithm: str = 'KMeans', 
                          **kwargs) -> Dict[str, Any]:
        """执行聚类分析"""
        
        # 检查数据是否为空
        if data.empty:
            raise ValueError("输入数据为空")
        
        # 检查数据维度
        if len(data) < 2:
            raise ValueError("数据点数量不足，至少需要2个数据点进行聚类")
        
        # 数据标准化
        scaled_data = self.scaler.fit_transform(data)
        
        # 根据算法选择聚类方法
        if algorithm == 'KMeans':
            n_clusters = kwargs.get('n_clusters', 3)
            # 确保聚类数量不超过数据点数量
            n_clusters = min(n_clusters, len(data))
            self.cluster_model = KMeans(
                n_clusters=n_clusters,
                init=kwargs.get('init', 'k-means++'),
                max_iter=kwargs.get('max_iter', 300),
                n_init=kwargs.get('n_init', 10),
                random_state=kwargs.get('random_state', 42)
            )
        elif algorithm == 'DBSCAN':
            self.cluster_model = DBSCAN(
                eps=kwargs.get('eps', 0.5),
                min_samples=kwargs.get('min_samples', 5)
            )
        elif algorithm == 'Agglomerative':
            n_clusters = kwargs.get('n_clusters', 3)
            n_clusters = min(n_clusters, len(data))
            self.cluster_model = AgglomerativeClustering(
                n_clusters=n_clusters,
                linkage=kwargs.get('linkage', 'ward')
            )
        else:
            raise ValueError(f"不支持的聚类算法: {algorithm}")
        
        # 执行聚类
        cluster_labels = self.cluster_model.fit_predict(scaled_data)
        
        # 计算聚类质量指标
        quality_metrics = self.calculate_cluster_quality(scaled_data, cluster_labels)
        
        # PCA降维用于可视化
        pca_data = self.pca.fit_transform(scaled_data)
        
        return {
            'cluster_labels': cluster_labels.tolist(),
            'silhouette_score': quality_metrics.get('silhouette_score', 0.0),  # 直接包含silhouette_score
            'calinski_harabasz_score': quality_metrics.get('calinski_harabasz_score', 0.0),
            'pca_data': pca_data.tolist(),
            'feature_importance': self.calculate_feature_importance(scaled_data, cluster_labels)
        }
    
    def calculate_cluster_quality(self, data: np.ndarray, labels: np.ndarray) -> Dict[str, float]:
        """计算聚类质量指标"""
        metrics = {}
        
        # 计算轮廓系数
        if len(np.unique(labels)) > 1:
            try:
                metrics['silhouette_score'] = float(silhouette_score(data, labels))
            except:
                metrics['silhouette_score'] = 0.0
        
        # 计算Calinski-Harabasz指数
        if len(np.unique(labels)) > 1:
            try:
                metrics['calinski_harabasz_score'] = float(calinski_harabasz_score(data, labels))
            except:
                metrics['calinski_harabasz_score'] = 0.0
        
        return metrics
    
    def calculate_feature_importance(self, data: np.ndarray, labels: np.ndarray) -> Dict[str, float]:
        """计算特征重要性"""
        importance = {}
        
        for i, feature in enumerate(self.feature_names):
            # 计算每个特征在不同聚类中的方差
            feature_values = data[:, i]
            importance[feature] = float(np.var(feature_values))
        
        # 归一化重要性分数
        total_importance = sum(importance.values())
        if total_importance > 0:
            importance = {k: v/total_importance for k, v in importance.items()}
        
        return importance
    
    def generate_cluster_profiles(self, data: pd.DataFrame, labels: np.ndarray) -> Dict[str, Any]:
        """生成聚类画像"""
        profiles = {}
        
        for cluster_id in np.unique(labels):
            if cluster_id == -1:  # DBSCAN的噪声点
                continue
                
            cluster_data = data[labels == cluster_id]
            profile = {
                'cluster_id': int(cluster_id),
                'size': int(len(cluster_data)),
                'percentage': float(len(cluster_data) / len(data) * 100),
                'characteristics': {}
            }
            
            # 计算每个特征的平均值和标准差
            for feature in self.feature_names:
                if feature in cluster_data.columns:
                    values = cluster_data[feature]
                    profile['characteristics'][feature] = {
                        'mean': float(values.mean()),
                        'std': float(values.std()),
                        'min': float(values.min()),
                        'max': float(values.max())
                    }
            
            profiles[f'cluster_{cluster_id}'] = profile
        
        return profiles

@academic_cluster.post('/academicClustering', description='学业特征聚类分析')
def academic_clustering(
    academic_data: List[Dict] = Body(..., embed=True),
    algorithm_config: Dict = Body(..., embed=True),
    weights: Optional[Dict] = Body(None, embed=True)
):
    """执行学业特征聚类分析"""
    
    try:
        print(f"收到聚类请求，数据量: {len(academic_data)}")
        print(f"算法配置: {algorithm_config}")
        print(f"权重配置: {weights}")
        
        # 创建分析器
        analyzer = AcademicClusterAnalyzer()
        
        # 准备数据
        feature_data = analyzer.prepare_data(academic_data, weights)
        
        # 执行聚类
        algorithm = algorithm_config.get('algorithm', 'KMeans')
        clustering_result = analyzer.perform_clustering(feature_data, algorithm, **algorithm_config.get('params', {}))
        
        # 生成聚类画像
        cluster_profiles = analyzer.generate_cluster_profiles(feature_data, clustering_result['cluster_labels'])
        
        # 将聚类结果添加到原始数据
        for i, item in enumerate(academic_data):
            item['clusterResult'] = clustering_result['cluster_labels'][i]
        
        result = {
            'data': json.dumps(academic_data, default=convert_numpy, ensure_ascii=False),
            'clustering_result': clustering_result,
            'cluster_profiles': cluster_profiles,
            'algorithm_used': algorithm,
            'weights_applied': weights or analyzer.default_weights
        }
        
        print(f"聚类完成，返回结果: {len(clustering_result['cluster_labels'])} 个聚类标签")
        return result
    
    except Exception as e:
        print(f"聚类分析失败: {str(e)}")
        import traceback
        traceback.print_exc()
        return {
            'error': str(e),
            'data': None
        }

@academic_cluster.post('/dynamicWeightClustering', description='动态权重聚类分析')
def dynamic_weight_clustering(
    academic_data: List[Dict] = Body(..., embed=True),
    stage_weights: Dict = Body(..., embed=True)
):
    """根据培养阶段进行动态权重聚类分析"""
    
    try:
        # 根据培养阶段调整权重
        stage = stage_weights.get('stage', 'general')
        weights = stage_weights.get('weights', {})
        
        # 不同培养阶段的权重策略
        if stage == 'first_year':
            # 第一年：重视基础学业表现
            weights.update({
                'academicPerformanceScore': 0.40,
                'researchProjectScore': 0.15,
                'thesisScore': 0.10,
                'academicPaperScore': 0.15,
                'competitionScore': 0.10,
                'patentSoftwareScore': 0.05,
                'otherInnovationAchievementScore': 0.05
            })
        elif stage == 'second_year':
            # 第二年：重视科研项目和论文
            weights.update({
                'academicPerformanceScore': 0.20,
                'researchProjectScore': 0.30,
                'thesisScore': 0.25,
                'academicPaperScore': 0.15,
                'competitionScore': 0.05,
                'patentSoftwareScore': 0.03,
                'otherInnovationAchievementScore': 0.02
            })
        elif stage == 'third_year':
            # 第三年：重视创新成果和综合能力
            weights.update({
                'academicPerformanceScore': 0.15,
                'researchProjectScore': 0.20,
                'thesisScore': 0.25,
                'academicPaperScore': 0.20,
                'competitionScore': 0.10,
                'patentSoftwareScore': 0.05,
                'otherInnovationAchievementScore': 0.05
            })
        
        # 执行聚类分析
        algorithm_config = {
            'algorithm': 'KMeans',
            'params': {
                'n_clusters': 3,
                'init': 'k-means++',
                'max_iter': 300,
                'n_init': 10,
                'random_state': 42
            }
        }
        
        return academic_clustering(academic_data, algorithm_config, weights)
    
    except Exception as e:
        return {
            'error': str(e),
            'data': None
        }

@academic_cluster.post('/radarChartData', description='生成雷达图数据')
def generate_radar_chart_data(
    student_data: Dict = Body(..., embed=True),
    average_data: Dict = Body(..., embed=True)
):
    """生成雷达图数据"""
    
    try:
        # 准备雷达图数据
        radar_data = {
            'student': {
                'name': student_data.get('graduateName', ''),
                'studentId': student_data.get('studentId', ''),
                'scores': {}
            },
            'average': {
                'scores': {}
            },
            'categories': [
                '学业表现', '科研项目', '论文质量', 
                '学术论文', '竞赛获奖', '专利软件', '创新成果'
            ]
        }
        
        # 映射字段名到中文
        field_mapping = {
            'academicPerformanceScore': '学业表现',
            'researchProjectScore': '科研项目',
            'thesisScore': '论文质量',
            'academicPaperScore': '学术论文',
            'competitionScore': '竞赛获奖',
            'patentSoftwareScore': '专利软件',
            'otherInnovationAchievementScore': '创新成果'
        }
        
        # 提取学生分数
        for field, chinese_name in field_mapping.items():
            try:
                score = float(student_data.get(field, 0))
                radar_data['student']['scores'][chinese_name] = score
            except (ValueError, TypeError):
                radar_data['student']['scores'][chinese_name] = 0.0
        
        # 提取平均分数
        for field, chinese_name in field_mapping.items():
            try:
                score = float(average_data.get(field, 0))
                radar_data['average']['scores'][chinese_name] = score
            except (ValueError, TypeError):
                radar_data['average']['scores'][chinese_name] = 0.0
        
        return {
            'data': radar_data,
            'success': True
        }
    
    except Exception as e:
        return {
            'error': str(e),
            'data': None,
            'success': False
        }

@academic_cluster.get('/clusterVisualization', description='生成聚类可视化数据')
def generate_cluster_visualization():
    """生成聚类可视化数据（示例）"""
    
    try:
        # 这里可以生成聚类可视化图表
        # 实际应用中可能需要保存图表文件或返回图表数据
        
        visualization_data = {
            'chart_type': 'scatter',
            'data': {
                'x': [1, 2, 3, 4, 5],
                'y': [2, 4, 1, 5, 3],
                'clusters': [0, 1, 0, 2, 1]
            },
            'title': '学业特征聚类可视化',
            'x_label': '主成分1',
            'y_label': '主成分2'
        }
        
        return {
            'data': visualization_data,
            'success': True
        }
    
    except Exception as e:
        return {
            'error': str(e),
            'data': None,
            'success': False
        }
