package com.bupt.middleware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author chenxiao
 * @date 2025/4/7 15:17
 * @description: Supervisor Wide Table Entity Class
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupvWideTable {

    private String sn; // 序列号

    private String supervisorName; // 姓名

    private String workId; // 工作证号（主键）

    private LocalDate entryDate; // 入职时间（年月日）

    private String citizenId; // 身份证号

    private String gender; // 性别

    private Integer currentAge; // 当前年龄

    private String cellPhoneNumber; // 手机号

    private String title; // 职称

    private Boolean isMasterSupervisor; // 是否是硕士研究生导师

    private Boolean isDoctorSupervisor; // 是否是博士研究生导师

    private String highestDegree; // 最高学位

    private String centerName; // 所属中心

    private String specialTitle; // 特殊荣誉

    private Integer masterNumber; // 硕士研究生在读人数

    private Integer doctorNumber; // 博士研究生在读人数

    private Integer masterDelayNumber; // 硕士研究生延毕人数

    private Integer doctorDelayNumber; // 博士研究生延毕人数

    private Double masterDelayRate; // 硕士研究生延毕比例

    private Double doctorDelayRate; // 博士研究生延毕比例

    private Integer excellentGraduatesNumber; // 优秀毕业生人数

    private Integer excellentGraduationThesis; // 优秀毕业论文

    private Integer prizeCount; // 在读生国奖获奖次数

    private Integer competitionCount; // 在读生竞赛获奖次数

    private Double employmentRate; // 就业率

    private Integer highLevelPaper; // 高水平论文数

    private Integer enterpriseProject; // 横向项目数

    private Integer governmentProject; // 纵向项目数

    private Double averageFund; // 人均经费数

    private Integer undergraduateCourse; // 承担本科生课程数

    private Integer graduateCourse; // 承担研究生课程数

    private Integer undergraduateCourseCredit; // 承担本科生学分数

    private Integer graduateCourseCredit; // 承担研究生学分数

    private Boolean isUniversityLeader; // 是否担任校领导

    private Boolean isCollegeLeader; // 是否担任院领导

    private Boolean isDean; // 是否担任系主任

    private Boolean isCenterDirector; // 是否担任中心主任

    private String firstYearRankRate; // 第一年绩效

    private String secondYearRankRate; // 第二年绩效

    private String thirdYearRankRate; // 第三年绩效
}
