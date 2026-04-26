package com.example.controller;

import com.example.pojo.ClassSchedule;
import com.example.pojo.dto.ClassScheduleDto;
import com.example.service.IClassScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 班级课表控制器
 */
@RestController
@RequestMapping("/api/class-schedules")
public class ClassScheduleController {
    
    @Autowired
    private IClassScheduleService classScheduleService;
    
    /**
     * 获取所有课程安排
     * @return 课程安排列表
     */
    @GetMapping
    public ResponseEntity<List<ClassSchedule>> getAllClassSchedules() {
        List<ClassSchedule> classSchedules = classScheduleService.getAllClassSchedules();
        return ResponseEntity.ok(classSchedules);
    }
    
    /**
     * 根据成绩ID获取课程安排
     * @param scoreId 成绩ID
     * @return 课程安排信息
     */
    @GetMapping("/{scoreId}")
    public ResponseEntity<ClassSchedule> getClassScheduleById(@PathVariable String scoreId) {
        ClassSchedule classSchedule = classScheduleService.getClassScheduleById(scoreId);
        if (classSchedule != null) {
            return ResponseEntity.ok(classSchedule);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据学生ID获取课程安排
     * @param studentId 学号
     * @return 该学生的课程安排列表
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ClassSchedule>> getClassSchedulesByStudentId(@PathVariable String studentId) {
        List<ClassSchedule> classSchedules = classScheduleService.getClassSchedulesByStudentId(studentId);
        return ResponseEntity.ok(classSchedules);
    }
    
    /**
     * 根据学期获取课程安排
     * @param semester 学期
     * @return 该学期的课程安排列表
     */
    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<ClassSchedule>> getClassSchedulesBySemester(@PathVariable String semester) {
        List<ClassSchedule> classSchedules = classScheduleService.getClassSchedulesBySemester(semester);
        return ResponseEntity.ok(classSchedules);
    }
    
    /**
     * 根据学生ID和学期获取课程安排
     * @param studentId 学号
     * @param semester 学期
     * @return 指定学生在指定学期的课程安排列表
     */
    @GetMapping("/student/{studentId}/semester/{semester}")
    public ResponseEntity<List<ClassSchedule>> getClassSchedulesByStudentIdAndSemester(
            @PathVariable String studentId, 
            @PathVariable String semester) {
        List<ClassSchedule> classSchedules = classScheduleService.getClassSchedulesByStudentIdAndSemester(studentId, semester);
        return ResponseEntity.ok(classSchedules);
    }
    
    /**
     * 保存课程安排
     * @param classScheduleDto 课程安排DTO
     * @return 保存后的课程安排
     */
    @PostMapping
    public ResponseEntity<ClassSchedule> saveClassSchedule(@RequestBody ClassScheduleDto classScheduleDto) {
        ClassSchedule savedClassSchedule = classScheduleService.saveClassSchedule(classScheduleDto);
        return ResponseEntity.ok(savedClassSchedule);
    }
    
    /**
     * 更新课程安排
     * @param scoreId 成绩ID
     * @param classScheduleDto 课程安排DTO
     * @return 更新后的课程安排
     */
    @PutMapping("/{scoreId}")
    public ResponseEntity<ClassSchedule> updateClassSchedule(
            @PathVariable String scoreId, 
            @RequestBody ClassScheduleDto classScheduleDto) {
        ClassSchedule updatedClassSchedule = classScheduleService.updateClassSchedule(scoreId, classScheduleDto);
        if (updatedClassSchedule != null) {
            return ResponseEntity.ok(updatedClassSchedule);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除课程安排
     * @param scoreId 成绩ID
     * @return 删除结果
     */
    @DeleteMapping("/{scoreId}")
    public ResponseEntity<Void> deleteClassSchedule(@PathVariable String scoreId) {
        classScheduleService.deleteClassSchedule(scoreId);
        return ResponseEntity.noContent().build();
    }
}
