package com.example.pojo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupvWideTableDto {

    // ============ 基本信息 ============
    @NotBlank(message = "工作证号不能为空")
    private String workId; // 工作证号（主键）

    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50字符")
    private String supervisorName; // 姓名

    @NotNull(message = "入职时间不能为空")
    private LocalDate entryDate; // 入职时间

    @NotBlank(message = "身份证号不能为空")
    //@Pattern(regexp = "^\\d{17}[\\dXx]$", message = "身份证号格式不正确")
    private String citizenId; // 身份证号

    @NotBlank(message = "性别不能为空")
    private String gender; // 性别

    @NotNull(message = "年龄不能为空")
    @Min(value = 18, message = "年龄不能小于18岁")
    @Max(value = 70, message = "年龄不能超过70岁")
    private Integer currentAge; // 当前年龄

    @NotBlank(message = "手机号不能为空")
    //@Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String cellPhoneNumber; // 手机号

    // ============ 教育信息 ============
    private String highestDegree; // 最高学位
    private String title; // 职称
    private String centerName; // 所属中心
    private String specialTitle; // 特殊荣誉

    // ============ 导师信息 ============
    private Boolean isMasterSupervisor; // 是否是硕士研究生导师
    private Boolean isDoctorSupervisor; // 是否是博士研究生导师

    // ============ 学生统计 ============
    @PositiveOrZero(message = "硕士在读人数不能为负数")
    private Integer masterNumber; // 硕士研究生在读人数

    @PositiveOrZero(message = "博士在读人数不能为负数")
    private Integer doctorNumber; // 博士研究生在读人数

    @PositiveOrZero(message = "硕士延毕人数不能为负数")
    private Integer masterDelayNumber; // 硕士研究生延毕人数

    @PositiveOrZero(message = "博士延毕人数不能为负数")
    private Integer doctorDelayNumber; // 博士研究生延毕人数

    @DecimalMin(value = "0.0", message = "延毕比例不能小于0")
    @DecimalMax(value = "1.0", message = "延毕比例不能大于1")
    private Double masterDelayRate; // 硕士研究生延毕比例

    @DecimalMin(value = "0.0", message = "延毕比例不能小于0")
    @DecimalMax(value = "1.000001", message = "延毕比例不能大于1")
    private Double doctorDelayRate; // 博士研究生延毕比例

    // ============ 学术成果 ============
    @PositiveOrZero(message = "优秀毕业生人数不能为负数")
    private Integer excellentGraduatesNumber; // 优秀毕业生人数

    @PositiveOrZero(message = "优秀毕业论文数不能为负数")
    private Integer excellentGraduationThesis; // 优秀毕业论文

    @PositiveOrZero(message = "国奖获奖次数不能为负数")
    private Integer prizeCount; // 在读生国奖获奖次数

    @PositiveOrZero(message = "竞赛获奖次数不能为负数")
    private Integer competitionCount; // 在读生竞赛获奖次数

    @DecimalMin(value = "0.0", message = "就业率不能小于0")
    @DecimalMax(value = "1.0", message = "就业率不能大于1")
    private Double employmentRate; // 就业率

    @PositiveOrZero(message = "高水平论文数不能为负数")
    private Integer highLevelPaper; // 高水平论文数

    // ============ 项目管理 ============
    @PositiveOrZero(message = "横向项目数不能为负数")
    private Integer enterpriseProject; // 横向项目数

    @PositiveOrZero(message = "纵向项目数不能为负数")
    private Integer governmentProject; // 纵向项目数

    @PositiveOrZero(message = "人均经费不能为负数")
    private Double averageFund; // 人均经费数

    // ============ 教学信息 ============
    @PositiveOrZero(message = "本科生课程数不能为负数")
    private Integer undergraduateCourse; // 承担本科生课程数

    @PositiveOrZero(message = "研究生课程数不能为负数")
    private Integer graduateCourse; // 承担研究生课程数

    @PositiveOrZero(message = "本科学分数不能为负数")
    private Integer undergraduateCourseCredit; // 承担本科生学分数

    @PositiveOrZero(message = "研究生学分数不能为负数")
    private Integer graduateCourseCredit; // 承担研究生学分数

    // ============ 行政职务 ============
    private Boolean isUniversityLeader; // 是否担任校领导
    private Boolean isCollegeLeader; // 是否担任院领导
    private Boolean isDean; // 是否担任系主任
    private Boolean isCenterDirector; // 是否担任中心主任

    // ============ 绩效信息 ============
    private String firstYearRankRate; // 第一年绩效
    private String secondYearRankRate; // 第二年绩效
    private String thirdYearRankRate; // 第三年绩效

//    public void setCitizenId(String citizenId) {
//        this.citizenId = citizenId != null ? citizenId.trim() : null;
//        this.citizenId = this.citizenId != null ?
//                citizenId.trim().toUpperCase().replace("Ｘ", "X") : null;
//    }
}