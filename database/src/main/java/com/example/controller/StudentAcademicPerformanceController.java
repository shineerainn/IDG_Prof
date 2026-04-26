package com.example.controller;

import com.example.pojo.StudentAcademicPerformance;
import com.example.pojo.dto.StudentAcademicPerformanceDto;
import com.example.service.IStudentAcademicPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/student-academic-performance")
public class StudentAcademicPerformanceController {

    @Autowired
    private IStudentAcademicPerformanceService studentAcademicPerformanceService;

    /**
     * 获取学生雷达图数据（指定学期）
     */
    @GetMapping("/radar/{studentId}/{year}/{semester}")
    public ResponseEntity<Map<String, Object>> getRadarData(
            @PathVariable String studentId,
            @PathVariable int year,
            @PathVariable int semester) {
        Map<String, Object> result = studentAcademicPerformanceService.getRadarData(studentId, year, semester);
        return ResponseEntity.ok(result);
    }


    /**
     * 添加单个学生学业成绩记录（指定学期）
     */
    @PostMapping("/{year}/{semester}")
    public ResponseEntity<StudentAcademicPerformance> add(
            @RequestBody StudentAcademicPerformanceDto dto,
            @PathVariable int year,
            @PathVariable int semester) {
        StudentAcademicPerformance result = studentAcademicPerformanceService.add(dto, year, semester);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量创建学生学业成绩记录（指定学期）
     */
    @PostMapping("/batch/{year}/{semester}")
    public ResponseEntity<List<StudentAcademicPerformance>> create(
            @RequestBody List<StudentAcademicPerformanceDto> dtos,
            @PathVariable int year,
            @PathVariable int semester) {
        List<StudentAcademicPerformance> results = studentAcademicPerformanceService.create(dtos, year, semester);
        return ResponseEntity.ok(results);
    }

    /**
     * 根据学号获取学生学业成绩记录（指定学期）
     */
    @GetMapping("/{studentId}/{year}/{semester}")
    public ResponseEntity<StudentAcademicPerformance> getByStudentId(
            @PathVariable String studentId,
            @PathVariable int year,
            @PathVariable int semester) {
        StudentAcademicPerformance result = studentAcademicPerformanceService.getByStudentId(studentId, year, semester);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取所有学生学业成绩记录（指定学期）
     */
    @GetMapping("/{year}/{semester}")
    public ResponseEntity<List<StudentAcademicPerformance>> getAll(
            @PathVariable int year,
            @PathVariable int semester) {
        List<StudentAcademicPerformance> results = studentAcademicPerformanceService.getAll(year, semester);
        return ResponseEntity.ok(results);
    }

    /**
     * 根据学号删除学生学业成绩记录（指定学期）
     */
    @DeleteMapping("/{studentId}/{year}/{semester}")
    public ResponseEntity<Void> delete(
            @PathVariable String studentId,
            @PathVariable int year,
            @PathVariable int semester) {
        studentAcademicPerformanceService.delete(studentId, year, semester);
        return ResponseEntity.noContent().build();
    }

    /**
     * 更新学生学业成绩记录（指定学期）
     */
    @PutMapping("/{year}/{semester}")
    public ResponseEntity<StudentAcademicPerformance> update(
            @RequestBody StudentAcademicPerformanceDto dto,
            @PathVariable int year,
            @PathVariable int semester) {
        StudentAcademicPerformance result = studentAcademicPerformanceService.update(dto, year, semester);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量更新学生学业成绩记录（指定学期）
     */
    @PutMapping("/batch/{year}/{semester}")
    public ResponseEntity<List<StudentAcademicPerformance>> batchUpdate(
            @RequestBody List<StudentAcademicPerformanceDto> dtos,
            @PathVariable int year,
            @PathVariable int semester) {
        List<StudentAcademicPerformance> results = studentAcademicPerformanceService.batchUpdate(dtos, year, semester);
        return ResponseEntity.ok(results);
    }
}
