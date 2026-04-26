package com.example.service;

import com.example.pojo.ClassSchedule;
import com.example.pojo.dto.ClassScheduleDto;

import java.util.List;

/**
 * 班级课表服务接口
 */
public interface IClassScheduleService {
    
    /**
     * 获取所有课程安排
     * @return 课程安排列表
     */
    List<ClassSchedule> getAllClassSchedules();
    
    /**
     * 根据成绩ID获取课程安排
     * @param scoreId 成绩ID
     * @return 课程安排信息
     */
    ClassSchedule getClassScheduleById(String scoreId);
    
    /**
     * 根据学生ID获取课程安排
     * @param studentId 学号
     * @return 该学生的课程安排列表
     */
    List<ClassSchedule> getClassSchedulesByStudentId(String studentId);
    
    /**
     * 根据学期获取课程安排
     * @param semester 学期
     * @return 该学期的课程安排列表
     */
    List<ClassSchedule> getClassSchedulesBySemester(String semester);
    
    /**
     * 根据学生ID和学期获取课程安排
     * @param studentId 学号
     * @param semester 学期
     * @return 指定学生在指定学期的课程安排列表
     */
    List<ClassSchedule> getClassSchedulesByStudentIdAndSemester(String studentId, String semester);
    
    /**
     * 保存课程安排
     * @param classScheduleDto 课程安排DTO
     * @return 保存后的课程安排
     */
    ClassSchedule saveClassSchedule(ClassScheduleDto classScheduleDto);
    
    /**
     * 更新课程安排
     * @param scoreId 成绩ID
     * @param classScheduleDto 课程安排DTO
     * @return 更新后的课程安排
     */
    ClassSchedule updateClassSchedule(String scoreId, ClassScheduleDto classScheduleDto);
    
    /**
     * 删除课程安排
     * @param scoreId 成绩ID
     */
    void deleteClassSchedule(String scoreId);
}
