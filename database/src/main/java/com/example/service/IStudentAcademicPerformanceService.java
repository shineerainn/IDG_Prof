package com.example.service;

import com.example.pojo.StudentAcademicPerformance;
import com.example.pojo.dto.StudentAcademicPerformanceDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IStudentAcademicPerformanceService {

    /**
     * 获取学生雷达图数据（指定学期）
     */
    Map<String, Object> getRadarData(String studentId, int year, int semester);

    /**
     * 新增学生学业数据（指定学期）
     */
    StudentAcademicPerformance add(StudentAcademicPerformanceDto dto, int year, int semester);

    /**
     * 批量新增学生学业数据（指定学期）
     */
    List<StudentAcademicPerformance> create(List<StudentAcademicPerformanceDto> dtos, int year, int semester);

    /**
     * 根据学生学号查询（指定学期）
     */
    StudentAcademicPerformance getByStudentId(String studentId, int year, int semester);

    /**
     * 查询全部信息（指定学期）
     */
    List<StudentAcademicPerformance> getAll(int year, int semester);

    /**
     * 根据学生学号删除学业信息（指定学期）
     */
    void delete(String studentId, int year, int semester);

    /**
     * 根据学生学号更新学业信息（指定学期）
     */
    StudentAcademicPerformance update(StudentAcademicPerformanceDto dto, int year, int semester);

    /**
     * 批量更新学生学业信息（指定学期）
     */
    List<StudentAcademicPerformance> batchUpdate(List<StudentAcademicPerformanceDto> dtos, int year, int semester);
}
