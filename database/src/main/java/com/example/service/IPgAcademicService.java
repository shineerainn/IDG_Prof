package com.example.service;

import com.example.pojo.PgAcademic;
import com.example.pojo.dto.PgAcademicDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPgAcademicService {
    
    /**
     * 新增学生学业数据
     */
    PgAcademic add(PgAcademicDto dto);
    
    /**
     * 批量新增学生学业数据
     */
    List<PgAcademic> create(List<PgAcademicDto> dtos);
    
    /**
     * 根据学生学号查询
     */
    PgAcademic getByStudentId(String studentId);
    
    /**
     * 查询全部信息
     */
    List<PgAcademic> getAll();
    
    /**
     * 根据学生学号删除学业信息
     */
    void delete(String studentId);
    
    /**
     * 根据学生学号更新学业信息
     */
    PgAcademic update(PgAcademicDto dto);
    
    /**
     * 批量更新学生学业信息
     */
    List<PgAcademic> batchUpdate(List<PgAcademicDto> dtos);
}
