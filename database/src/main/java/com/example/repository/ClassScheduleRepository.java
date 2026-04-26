package com.example.repository;

import com.example.pojo.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 班级课表仓库接口
 */
@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, String> {
    
    /**
     * 根据学生ID查找课程安排
     * @param studentId 学号
     * @return 该学生的课程安排列表
     */
    List<ClassSchedule> findByStudentId(String studentId);
    
    /**
     * 根据学期查找课程安排
     * @param semester 学期
     * @return 该学期的课程安排列表
     */
    List<ClassSchedule> findBySemester(String semester);
    
    /**
     * 根据学生ID和学期查找课程安排
     * @param studentId 学号
     * @param semester 学期
     * @return 指定学生在指定学期的课程安排列表
     */
    List<ClassSchedule> findByStudentIdAndSemester(String studentId, String semester);
}
