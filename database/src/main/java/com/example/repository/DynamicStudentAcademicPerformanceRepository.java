package com.example.repository;

import com.example.pojo.StudentAcademicPerformance;
import com.example.util.TableNameUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DynamicStudentAcademicPerformanceRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * 根据年份和学期保存数据
     */
    public StudentAcademicPerformance save(StudentAcademicPerformance entity, int year, int semester) {
        String tableName = TableNameUtils.generateTableName("student_academic_performance", year, semester);
        
        // 构建插入SQL
        String sql = "INSERT INTO " + tableName + " (...) VALUES (...)"; // 根据实际字段构建SQL
        // 执行原生SQL
        entityManager.createNativeQuery(sql).executeUpdate();
        return entity;
    }
    
    /**
     * 根据年份和学期查询数据
     */
    public List<StudentAcademicPerformance> findAll(int year, int semester) {
        String tableName = TableNameUtils.generateTableName("student_academic_performance", year, semester);
        String sql = "SELECT * FROM " + tableName;
        return entityManager.createNativeQuery(sql, StudentAcademicPerformance.class).getResultList();
    }
    
    /**
     * 根据学号和学期查询数据
     */
    public StudentAcademicPerformance findByStudentId(String studentId, int year, int semester) {
        String tableName = TableNameUtils.generateTableName("student_academic_performance", year, semester);
        String sql = "SELECT * FROM " + tableName + " WHERE student_id = :studentId";
        List<StudentAcademicPerformance> results = entityManager
            .createNativeQuery(sql, StudentAcademicPerformance.class)
            .setParameter("studentId", studentId)
            .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}
