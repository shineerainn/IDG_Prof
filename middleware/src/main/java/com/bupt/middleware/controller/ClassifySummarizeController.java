package com.bupt.middleware.controller;

import com.bupt.middleware.entity.PgWideTable;
import com.bupt.middleware.entity.result.Result;
import com.bupt.middleware.service.IClassifySummarizeService;
import com.bupt.middleware.service.IPgWideTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenxiao
 * @date 2025/4/8 17:03
 * @description: 分类汇总，计算完善数据宽表（4个健康指数）
 */

@RestController
@RequestMapping("/clasSum")
@Slf4j
public class ClassifySummarizeController {

    @Autowired
    private IClassifySummarizeService classifySummarizeService;

    @Autowired
    private IPgWideTableService pgWideTableService;

    // 更新宽表数据(健康指数)
    @GetMapping("/updateIndex")
    public Result<List<PgWideTable>> updateIndex() {
        log.info("Starting to update health indices for wide table data");

        try {
            log.debug("Fetching all postgraduate wide table data");
            List<PgWideTable> pgWideTables = pgWideTableService.getAllPg().getData();
            log.info("Successfully fetched {} postgraduate wide table records", pgWideTables.size());

            log.debug("Calculating and updating health indices");
            List<PgWideTable> newPgWideTables = classifySummarizeService.updateIndex(pgWideTables);
            log.info("Health indices calculated for {} records", newPgWideTables.size());

            log.debug("Saving updated wide table data");
            Result<String> saveResult = pgWideTableService.updatePgs(newPgWideTables);

            if (saveResult.getCode() == 200) {
                log.info("Successfully updated {} wide table records with new health indices",
                        newPgWideTables.size());
                return Result.success(newPgWideTables, "success");
            } else {
                log.error("Failed to update wide table records. Error code: {}, Message: {}",
                        saveResult.getCode(), saveResult.getMsg());
                return Result.error(saveResult.getCode(), saveResult.getMsg());
            }
        } catch (Exception e) {
            log.error("Error occurred while updating health indices: {}", e.getMessage(), e);
            throw e;
        }
    }
}