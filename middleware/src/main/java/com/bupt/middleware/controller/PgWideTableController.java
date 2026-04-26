package com.bupt.middleware.controller;

import com.bupt.middleware.entity.PgWideTable;
import com.bupt.middleware.entity.result.Result;
import com.bupt.middleware.service.IPgWideTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenxiao
 * @date 2025/4/8 17:40
 * @description: Postgraduate Wide Table Controller
 */
@RestController
@RequestMapping("/wideTable")
@Slf4j
public class PgWideTableController {

    @Autowired
    private IPgWideTableService pgWideTableService;

    // 获取学生宽表数据（By Id）
    @GetMapping("/getPg")
    public Result<PgWideTable> getPg(@RequestParam(value = "pgId") String pgId) {
        log.info("Fetching wide table data for postgraduate ID: {}", pgId);
        try {
            Result<PgWideTable> result = pgWideTableService.getPg(pgId);
            if (result.getCode() == 200 && result.getData() != null) {
                log.info("Successfully fetched wide table data for postgraduate ID: {}", pgId);
            } else {
                log.warn("No wide table data found for postgraduate ID: {}", pgId);
            }
            return result;
        } catch (Exception e) {
            log.error("Error fetching wide table data for postgraduate ID: {}. Error: {}",
                    pgId, e.getMessage(), e);
            throw e;
        }
    }

    // 获取全部学生宽表数据
    @GetMapping("/getAllPg")
    public Result<List<PgWideTable>> getAllPg() {
        log.info("Fetching all postgraduate wide table data");
        try {
            Result<List<PgWideTable>> result = pgWideTableService.getAllPg();
            if (result.getCode() == 200 && result.getData() != null) {
                log.info("Successfully fetched {} postgraduate wide table records",
                        result.getData().size());
            } else {
                log.warn("No wide table data found or error occurred");
            }
            return result;
        } catch (Exception e) {
            log.error("Error fetching all postgraduate wide table data. Error: {}",
                    e.getMessage(), e);
            throw e;
        }
    }

    // 更新学生宽表数据
    @PutMapping("/updatePg")
    public Result<String> updatePg(@RequestBody PgWideTable dto) {
        log.info("Updating wide table data for postgraduate ID: {}", dto.getStudentId());
        try {
            Result<String> result = pgWideTableService.updatePg(dto);
            if (result.getCode() == 200) {
                log.info("Successfully updated wide table data for postgraduate ID: {}",
                        dto.getStudentId());
            } else {
                log.warn("Failed to update wide table data for postgraduate ID: {}. Error: {}",
                        dto.getStudentId(), result.getMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("Error updating wide table data for postgraduate ID: {}. Error: {}",
                    dto.getStudentId(), e.getMessage(), e);
            throw e;
        }
    }
}