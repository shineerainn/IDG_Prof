package com.bupt.middleware.service;

import com.bupt.middleware.entity.AlgorithmConfig;
import com.bupt.middleware.entity.SupvProfileRecord;
import com.bupt.middleware.entity.SupvWideTable;
import com.bupt.middleware.entity.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenxiao
 * @date 2025/4/9 16:43
 * @description: Interface of Supervisor Profile Service
 */

@Service
public interface ISupvProfileService {
    /**
     * 聚类生成导师画像
     * param: 属性列表，教师宽表数据，算法配置表
     * return: 教师ID与聚类结果Map
     */
    List<Map<String, Object>> buildSupvProfile(List<String> attrList, List<SupvWideTable> supvWideTables, AlgorithmConfig algorithmConfig);

    /**
     * 画像列表查询
     * param: userId
     * return: List<SupvProfileRecord>
     */
    Result<List<SupvProfileRecord>> getSupvProfileRecords(String userId);

    /**
     * 画像Id查询记录
     * param: profileId
     * return: SupvProfileRecord
     */
    Result<SupvProfileRecord> getSupvProfileRecord(String profileId);

    /**
     * 画像记录存储
     * param: SupvProfileRecord
     * return: Result<String>
     */
    Result<String> postSupvProfileRecord(SupvProfileRecord supvProfileRecord);
}
