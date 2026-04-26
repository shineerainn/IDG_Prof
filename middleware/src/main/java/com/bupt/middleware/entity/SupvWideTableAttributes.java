package com.bupt.middleware.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenxiao
 * @date 2025/4/13 14:31
 * @description: Supervisor Wide Table Attributes
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupvWideTableAttributes {
    @JsonProperty(value = "supervisorName")
    private Boolean supervisorName; // 姓名

    @JsonProperty(value = "workId")
    private Boolean workId; // 工作证号（主键）

    @JsonProperty(value = "entryDate")
    private Boolean entryDate; // 入职时间（年月日）

    @JsonProperty(value = "citizenId")
    private Boolean citizenId; // 身份证号

    @JsonProperty(value = "gender")
    private Boolean gender; // 性别

    @JsonProperty(value = "currentAge")
    private Boolean currentAge; // 当前年龄

    @JsonProperty(value = "cellPhoneNumber")
    private Boolean cellPhoneNumber; // 手机号

    @JsonProperty(value = "title")
    private Boolean title; // 职称

    @JsonProperty(value = "isMasterSupervisor")
    private Boolean isMasterSupervisor; // 是否是硕士研究生导师

    @JsonProperty(value = "isDoctorSupervisor")
    private Boolean isDoctorSupervisor; // 是否是博士研究生导师

    @JsonProperty(value = "highestDegree")
    private Boolean highestDegree; // 最高学位

    @JsonProperty(value = "centerName")
    private Boolean centerName; // 所属中心

    @JsonProperty(value = "specialTitle")
    private Boolean specialTitle; // 特殊荣誉

    @JsonProperty(value = "masterNumber")
    private Boolean masterNumber; // 硕士研究生在读人数

    @JsonProperty(value = "doctorNumber")
    private Boolean doctorNumber; // 博士研究生在读人数

    @JsonProperty(value = "masterDelayNumber")
    private Boolean masterDelayNumber; // 硕士研究生延毕人数

    @JsonProperty(value = "doctorDelayNumber")
    private Boolean doctorDelayNumber; // 博士研究生延毕人数

    @JsonProperty(value = "masterDelayRate")
    private Boolean masterDelayRate; // 硕士研究生延毕比例

    @JsonProperty(value = "doctorDelayRate")
    private Boolean doctorDelayRate; // 博士研究生延毕比例

    @JsonProperty(value = "excellentGraduatesNumber")
    private Boolean excellentGraduatesNumber; // 优秀毕业生人数

    @JsonProperty(value = "excellentGraduationThesis")
    private Boolean excellentGraduationThesis; // 优秀毕业论文

    @JsonProperty(value = "prizeCount")
    private Boolean prizeCount; // 在读生国奖获奖次数

    @JsonProperty(value = "competitionCount")
    private Boolean competitionCount; // 在读生竞赛获奖次数

    @JsonProperty(value = "employmentRate")
    private Boolean employmentRate; // 就业率

    @JsonProperty(value = "highLevelPaper")
    private Boolean highLevelPaper; // 高水平论文数

    @JsonProperty(value = "enterpriseProject")
    private Boolean enterpriseProject; // 横向项目数

    @JsonProperty(value = "governmentProject")
    private Boolean governmentProject; // 纵向项目数

    @JsonProperty(value = "averageFund")
    private Boolean averageFund; // 人均经费数

    @JsonProperty(value = "undergraduateCourse")
    private Boolean undergraduateCourse; // 承担本科生课程数

    @JsonProperty(value = "graduateCourse")
    private Boolean graduateCourse; // 承担研究生课程数

    @JsonProperty(value = "undergraduateCourseCredit")
    private Boolean undergraduateCourseCredit; // 承担本科生学分数

    @JsonProperty(value = "graduateCourseCredit")
    private Boolean graduateCourseCredit; // 承担研究生学分数

    @JsonProperty(value = "isUniversityLeader")
    private Boolean isUniversityLeader; // 是否担任校领导

    @JsonProperty(value = "isCollegeLeader")
    private Boolean isCollegeLeader; // 是否担任院领导

    @JsonProperty(value = "isDean")
    private Boolean isDean; // 是否担任系主任

    @JsonProperty(value = "isCenterDirector")
    private Boolean isCenterDirector; // 是否担任中心主任

    @JsonProperty(value = "firstYearRankRate")
    private Boolean firstYearRankRate; // 第一年绩效

    @JsonProperty(value = "secondYearRankRate")
    private Boolean secondYearRankRate; // 第二年绩效

    @JsonProperty(value = "thirdYearRankRate")
    private Boolean thirdYearRankRate; // 第三年绩效
}
