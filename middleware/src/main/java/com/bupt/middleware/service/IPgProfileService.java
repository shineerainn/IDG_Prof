package com.bupt.middleware.service;

import com.bupt.middleware.entity.AlgorithmConfig;
import com.bupt.middleware.entity.PgProfileRecord;
import com.bupt.middleware.entity.PgWideTable;
import com.bupt.middleware.entity.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenxiao
 * @date 2025/4/8 18:50
 * @description: Interface of Postgraduate Profile Service
 */

@Service
public interface IPgProfileService {
    /**
     * 聚类生成学生画像
     * param: 属性列表，学生宽表数据，算法配置表
     * return: 学生宽表+聚类结果
     */
    List<Map<String, Object>> buildPgProfile(List<String> attrList, List<PgWideTable> pgWideTables, AlgorithmConfig algorithmConfig);

    /**
     * 画像列表查询
     * param: userId
     * return: List<PgProfileRecord>
     */
    Result<List<PgProfileRecord>> getPgProfileRecords(String userId);

    /**
     * 画像Id查询记录
     * param: profileId
     * return: PgProfileRecord
     */
    Result<PgProfileRecord> getPgProfileRecord(String profileId);

    /**
     * 画像记录存储
     * param: PgProfileRecord
     * return: Result<String>
     */
    Result<String> postPgProfileRecord(PgProfileRecord pgProfileRecord);
}
