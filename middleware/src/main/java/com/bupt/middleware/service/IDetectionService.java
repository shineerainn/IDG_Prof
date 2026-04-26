package com.bupt.middleware.service;

import com.bupt.middleware.entity.AlgorithmConfig;
import com.bupt.middleware.entity.PgDetectionRecord;
import com.bupt.middleware.entity.PgWideTable;
import com.bupt.middleware.entity.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenxiao
 * @date 2025/4/9 17:11
 * @description: Interface of Detection Service
 */

@Service
public interface IDetectionService {
    /**
     * 训练预警分类模型
     * param: 属性列表，学生宽表数据，算法配置表
     * return: 模型名称
     */
    String buildDetectionModel(List<String> attList, List<PgWideTable> pgWideTables, AlgorithmConfig algorithmConfig);

    /**
     * 预警预测
     * param: 属性列表，模型名称
     * return: 学生宽表+分类结果（置信度）
     */
    List<Map<String, Object>> inferDetectionModel(List<PgWideTable> pgWideTables, String modelName);

    /**
     * 预警列表查询
     * param: userId
     * return: Result<List<PgDetectionRecord>>
     */
    Result<List<PgDetectionRecord>> getDetectionRecords(String userId);

    /**
     * 预警记录查询（单条）
     * param: detectionId
     * return: Result<PgDetectionRecord>
     */
    Result<PgDetectionRecord> getDetectionRecord(String detectionId);

    /**
     * 预警记录存储
     * param: PgDetectionRecord
     * return: Result<String>
     */
    Result<String> postDetectionRecord(PgDetectionRecord pgDetectionRecord);
}
