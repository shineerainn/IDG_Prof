package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "class_schedule")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassSchedule {

    @Id
    @Column(name = "score_id")
    private String scoreId; // 成绩id（主键）

    @Column(name = "student_name")
    private String studentName; // 姓名

    @Column(name = "student_id")
    private String studentId; // 学号

    @Column(name = "course_id")
    private String courseId; // 课程编号

    @Column(name = "course_name")
    private String courseName; // 课程名称

    @Column(name = "credit_hours")
    private Integer creditHours; // 学时

    @Column(name = "credits")
    private Double credits; // 学分

    @Column(name = "semester")
    private String semester; // 学期

    @Column(name = "final_score")
    private Double finalScore; // 期末成绩

    @Column(name = "overall_score")
    private Double overallScore; // 综合成绩

    @Column(name = "class_rank")
    private Integer classRank; // 班级排名

    @Column(name = "class_size")
    private Integer classSize; // 班级人数

    @Column(name = "category")
    private String category; // 类别

    @Column(name = "attribute")
    private String attribute; // 属性
}