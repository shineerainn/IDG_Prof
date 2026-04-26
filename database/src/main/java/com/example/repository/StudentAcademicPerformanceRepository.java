package com.example.repository;

import com.example.pojo.StudentAcademicPerformance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StudentAcademicPerformanceRepository extends CrudRepository<StudentAcademicPerformance, String> {

    /**
     * 根据学生学号查找学业信息记录
     */
    Optional<StudentAcademicPerformance> findByStudentId(String studentId);

    /**
     * 检查指定学号的记录是否存在
     */
    boolean existsByStudentId(String studentId);

    /**
     * 根据学生学号删除学业信息记录
     */
    void deleteByStudentId(String studentId);
}
