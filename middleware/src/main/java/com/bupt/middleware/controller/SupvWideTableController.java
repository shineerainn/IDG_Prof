package com.bupt.middleware.controller;

import com.bupt.middleware.entity.SupvWideTable;
import com.bupt.middleware.entity.result.Result;
import com.bupt.middleware.service.SupvWideTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenxiao
 * @date 2025/4/5 00:51
 * @description: Supervisor Wide Table Controller
 */
@RestController
@RequestMapping("/wideTable")
@Slf4j
public class SupvWideTableController {

    @Autowired
    private SupvWideTableService supvWideTableService;

    // 获取导师宽表数据（By Id）
    @GetMapping("/getSupv")
    public Result<SupvWideTable> getSupv(@RequestParam(value = "workId") String workId) {
        log.info("Starting to fetch supervisor wide table data for workId: {}", workId);
        try {
            Result<SupvWideTable> result = supvWideTableService.getSupv(workId);
            if (result.getCode() == 200) {
                if (result.getData() != null) {
                    log.info("Successfully fetched supervisor data for workId: {}", workId);
                } else {
                    log.warn("No data found for supervisor with workId: {}", workId);
                }
            } else {
                log.error("Failed to fetch supervisor data for workId: {}. Error: {}",
                        workId, result.getMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("Exception occurred while fetching supervisor data for workId: {}. Error: {}",
                    workId, e.getMessage(), e);
            throw e;
        }
    }

    // 获取全部导师宽表数据
    @GetMapping("/getAllSupv")
    public Result<List<SupvWideTable>> getAllSupv() {
        log.info("Starting to fetch all supervisor wide table data");
        try {
            Result<List<SupvWideTable>> result = supvWideTableService.getAllSupv();
            if (result.getCode() == 200) {
                if (result.getData() != null) {
                    log.info("Successfully fetched {} supervisor records", result.getData().size());
                } else {
                    log.warn("No supervisor data found in database");
                }
            } else {
                log.error("Failed to fetch all supervisor data. Error: {}", result.getMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("Exception occurred while fetching all supervisor data. Error: {}",
                    e.getMessage(), e);
            throw e;
        }
    }

    // 更新导师宽表数据
    @PutMapping("/updateSupv")
    public Result<String> updateSupv(@RequestBody SupvWideTable dto) {
        log.info("Starting to update supervisor data for workId: {}", dto.getWorkId());
        try {
            Result<String> result = supvWideTableService.updateSupv(dto);
            if (result.getCode() == 200) {
                log.info("Successfully updated supervisor data for workId: {}", dto.getWorkId());
            } else {
                log.error("Failed to update supervisor data for workId: {}. Error: {}",
                        dto.getWorkId(), result.getMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("Exception occurred while updating supervisor data for workId: {}. Error: {}",
                    dto.getWorkId(), e.getMessage(), e);
            throw e;
        }
    }
}