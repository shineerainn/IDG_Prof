package com.bupt.middleware.service;

import com.bupt.middleware.entity.PgWideTable;
import com.bupt.middleware.entity.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenxiao
 * @date 2025/4/8 17:21
 * @description: Interface of Postgraduate Wide Table Controller
 */

@Service
public interface IPgWideTableService {
    /**
     * 根据pgId查询学生宽表数据
     */
    Result<PgWideTable> getPg(String pgId);

    /**
     * 查询全部数据
     */
    Result<List<PgWideTable>> getAllPg();

    /**
     * 更新学生宽表数据
     */
    Result<String> updatePg(PgWideTable dto);

    /**
     * 批量更新学生宽表数据
     */
    Result<String> updatePgs(List<PgWideTable> pgWideTables);

}
