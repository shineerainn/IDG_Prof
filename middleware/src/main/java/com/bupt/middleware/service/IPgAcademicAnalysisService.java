package com.bupt.middleware.service;

import com.bupt.middleware.entity.AlgorithmConfig;
import com.bupt.middleware.entity.PgAcademic;
import com.bupt.middleware.entity.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface IPgAcademicAnalysisService {
    
    /**
     * 计算学生各项指标分数
     * @param studentId 学生ID
     * @return 计算后的学业数据
     */
    Result<PgAcademic> calculateStudentScores(String studentId);
    
    /**
     * 批量计算所有学生的各项指标分数
     * @return 计算后的学业数据列表
     */
    Result<List<PgAcademic>> calculateAllStudentScores();
    
    /**
     * 执行学业特征聚类分析
     * @param algorithm 聚类算法配置
     * @return 聚类结果
     */
    Result<Map<String, Object>> performAcademicClustering(Map<String, Object> algorithm);
    
    /**
     * 聚类生成学业画像
     * @param attrList 属性列表
     * @param pgAcademics 学业数据列表
     * @param algorithmConfig 算法配置
     * @return 聚类结果列表
     */
    List<Map<String, Object>> buildAcademicProfile(List<String> attrList, List<PgAcademic> pgAcademics, AlgorithmConfig algorithmConfig);
    
    /**
     * 获取学生雷达图数据（个人得分 vs 同年级平均分）
     * @param studentId 学生ID
     * @return 雷达图数据
     */
    Result<Map<String, Object>> getStudentRadarChartData(String studentId);
    
    /**
     * 获取同年级学生平均分数据
     * @param grade 年级
     * @return 平均分数据
     */
    Result<Map<String, Object>> getGradeAverageScores(String grade);
    
    /**
     * 动态调整特征权重
     * @param studentId 学生ID
     * @param weights 权重配置
     * @return 调整后的分数
     */
    Result<PgAcademic> adjustFeatureWeights(String studentId, Map<String, Double> weights);
}
