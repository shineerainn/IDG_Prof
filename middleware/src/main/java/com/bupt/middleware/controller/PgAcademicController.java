package com.bupt.middleware.controller;

import com.bupt.middleware.entity.PgAcademic;
import com.bupt.middleware.entity.result.Result;
import com.bupt.middleware.service.IPgAcademicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/academic")
@Slf4j
public class PgAcademicController {

    @Autowired
    private IPgAcademicService pgAcademicService;

    // 获取学生学业数据（By StudentId）
   @GetMapping("/getPg")
   public Result<PgAcademic> getPg(@RequestParam(value = "studentId") String studentId) {
       log.info("Fetching academic data for student ID: {}", studentId);
       try {
           Result<PgAcademic> result = pgAcademicService.getPg(studentId);
           if (result.getCode() == 200 && result.getData() != null) {
               log.info("Successfully fetched academic data for student ID: {}", studentId);
           } else {
               log.warn("No academic data found for student ID: {}", studentId);
           }
           return result;
       } catch (Exception e) {
           log.error("Error fetching academic data for student ID: {}. Error: {}",
                   studentId, e.getMessage(), e);
           throw e;
       }
   }


    // 获取全部学生学业数据
    @GetMapping("/getAllPg")
    public Result<List<PgAcademic>> getAllPg() {
        log.info("Fetching all postgraduate academic data");
        try {
            Result<List<PgAcademic>> result = pgAcademicService.getAllPg();
            if (result.getCode() == 200 && result.getData() != null) {
                log.info("Successfully fetched {} postgraduate academic records",
                        result.getData().size());
            } else {
                log.warn("No academic data found or error occurred");
            }
            return result;
        } catch (Exception e) {
            log.error("Error fetching all postgraduate academic data. Error: {}",
                    e.getMessage(), e);
            throw e;
        }
    }

    // 更新学生学业数据
    @PutMapping("/updatePg")
    public Result<String> updatePg(@RequestBody PgAcademic dto) {
        log.info("Updating academic data for student ID: {}", dto.getStudentId());
        try {
            Result<String> result = pgAcademicService.updatePg(dto);
            if (result.getCode() == 200) {
                log.info("Successfully updated academic data for student ID: {}",
                        dto.getStudentId());
            } else {
                log.warn("Failed to update academic data for student ID: {}. Error: {}",
                        dto.getStudentId(), result.getMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("Error updating academic data for student ID: {}. Error: {}",
                    dto.getStudentId(), e.getMessage(), e);
            throw e;
        }
    }

    // 批量更新学生学业数据
    @PostMapping("/updatePgs")
    public Result<String> updatePgs(@RequestBody List<PgAcademic> pgAcademics) {
        log.info("Batch updating academic data for {} students", pgAcademics.size());
        try {
            Result<String> result = pgAcademicService.updatePgs(pgAcademics);
            if (result.getCode() == 200) {
                log.info("Successfully batch updated academic data for {} students",
                        pgAcademics.size());
            } else {
                log.warn("Failed to batch update academic data. Error: {}", result.getMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("Error batch updating academic data. Error: {}", e.getMessage(), e);
            throw e;
        }
    }
}
