package com.bupt.middleware.service;

import com.bupt.middleware.entity.SupvWideTable;
import com.bupt.middleware.entity.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenxiao
 * @date 2025/4/8 17:50
 * @description: Interface of Supervisor Wide Table Service
 */

@Service
public interface ISupvWideTableService {
    /**
     * 根据workId查询导师宽表数据
     */
    Result<SupvWideTable> getSupv(String workId);

    /**
     * 查询全部数据
     */
    Result<List<SupvWideTable>> getAllSupv();

    /**
     * 更新导师宽表数据
     */
    Result<String> updateSupv(SupvWideTable dto);
}
