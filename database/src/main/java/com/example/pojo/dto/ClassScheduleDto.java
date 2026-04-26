package com.example.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassScheduleDto {

    @NotBlank(message = "成绩ID不能为空")
    private String scoreId; // 成绩id（主键）

    @NotBlank(message = "学生姓名不能为空")
    private String studentName; // 姓名

    @NotBlank(message = "学号不能为空")
    private String studentId; // 学号

    @NotBlank(message = "课程编号不能为空")
    private String courseId; // 课程编号

    @NotBlank(message = "课程名称不能为空")
    private String courseName; // 课程名称

    @NotNull(message = "学时不能为空")
    private Integer creditHours; // 学时

    @NotNull(message = "学分不能为空")
    private Double credits; // 学分

    @NotBlank(message = "学期不能为空")
    private String semester; // 学期

    @NotNull(message = "期末成绩不能为空")
    private Double finalScore; // 期末成绩

    @NotNull(message = "综合成绩不能为空")
    private Double overallScore; // 综合成绩

    @NotNull(message = "班级排名不能为空")
    private Integer classRank; // 班级排名

    @NotNull(message = "班级人数不能为空")
    private Integer classSize; // 班级人数

    @NotBlank(message = "类别不能为空")
    private String category; // 类别

    @NotBlank(message = "属性不能为空")
    private String attribute; // 属性
}
