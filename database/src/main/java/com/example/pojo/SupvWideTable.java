package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "supv_wide_table")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupvWideTable {

    @Column(name = "sn")
    private String sn; // 序列号

    @Column(name = "supervisor_name")
    private String supervisorName; // 姓名

    @Id
    @Column(name = "work_id")
    private String workId; // 工作证号（主键）

    @Column(name = "entry_date")
    private LocalDate entryDate; // 入职时间（年月日）

    @Column(name = "citizen_id")
    private String citizenId; // 身份证号

    @Column(name = "gender")
    private String gender; // 性别

    @Column(name = "current_age")
    private Integer currentAge; // 当前年龄

    @Column(name = "cell_phone_number")
    private String cellPhoneNumber; // 手机号

    @Column(name = "title")
    private String title; // 职称

    @Column(name = "is_master_supervisor")
    private Boolean isMasterSupervisor; // 是否是硕士研究生导师

    @Column(name = "is_doctor_supervisor")
    private Boolean isDoctorSupervisor; // 是否是博士研究生导师

    @Column(name = "highest_degree")
    private String highestDegree; // 最高学位

    @Column(name = "center_name")
    private String centerName; // 所属中心

    @Column(name = "special_title")
    private String specialTitle; // 特殊荣誉

    @Column(name = "master_number")
    private Integer masterNumber; // 硕士研究生在读人数

    @Column(name = "doctor_number")
    private Integer doctorNumber; // 博士研究生在读人数

    @Column(name = "master_delay_number")
    private Integer masterDelayNumber; // 硕士研究生延毕人数

    @Column(name = "doctor_delay_number")
    private Integer doctorDelayNumber; // 博士研究生延毕人数

    @Column(name = "master_delay_rate")
    private Double masterDelayRate; // 硕士研究生延毕比例

    @Column(name = "doctor_delay_rate")
    private Double doctorDelayRate; // 博士研究生延毕比例

    @Column(name = "excellent_graduates_number")
    private Integer excellentGraduatesNumber; // 优秀毕业生人数

    @Column(name = "excellent_graduation_thesis")
    private Integer excellentGraduationThesis; // 优秀毕业论文

    @Column(name = "prize_count")
    private Integer prizeCount; // 在读生国奖获奖次数

    @Column(name = "competition_count")
    private Integer competitionCount; // 在读生竞赛获奖次数

    @Column(name = "employment_rate")
    private Double employmentRate; // 就业率

    @Column(name = "high_level_paper")
    private Integer highLevelPaper; // 高水平论文数

    @Column(name = "enterprise_project")
    private Integer enterpriseProject; // 横向项目数

    @Column(name = "government_project")
    private Integer governmentProject; // 纵向项目数

    @Column(name = "average_fund")
    private Double averageFund; // 人均经费数

    @Column(name = "undergraduate_course")
    private Integer undergraduateCourse; // 承担本科生课程数

    @Column(name = "graduate_course")
    private Integer graduateCourse; // 承担研究生课程数

    @Column(name = "undergraduate_course_credit")
    private Integer undergraduateCourseCredit; // 承担本科生学分数

    @Column(name = "graduate_course_credit")
    private Integer graduateCourseCredit; // 承担研究生学分数

    @Column(name = "is_university_leader")
    private Boolean isUniversityLeader; // 是否担任校领导

    @Column(name = "is_college_leader")
    private Boolean isCollegeLeader; // 是否担任院领导

    @Column(name = "is_dean")
    private Boolean isDean; // 是否担任系主任

    @Column(name = "is_center_director")
    private Boolean isCenterDirector; // 是否担任中心主任

    @Column(name = "first_year_rank_rate")
    private String firstYearRankRate; // 第一年绩效

    @Column(name = "second_year_rank_rate")
    private String secondYearRankRate; // 第二年绩效

    @Column(name = "third_year_rank_rate")
    private String thirdYearRankRate; // 第三年绩效

}