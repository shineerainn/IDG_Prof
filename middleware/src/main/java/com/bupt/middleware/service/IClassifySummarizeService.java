package com.bupt.middleware.service;

import com.bupt.middleware.entity.PgWideTable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenxiao
 * @date 2025/4/8 18:24
 * @description: Interface of Classify and Summarize Service
 */

@Service
public interface IClassifySummarizeService {
    /**
     * 计算健康指数
     */
    List<PgWideTable> updateIndex(List<PgWideTable> pgWideTables);
}
