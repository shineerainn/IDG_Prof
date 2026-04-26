package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "pg_wide_table")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PgWideTable {

    @Column(name = "sn")
    private String sn; // 1、序列号

    @Column(name = "graduate_name")
    private String graduateName; // 2、姓名

    @Id
    @Column(name = "student_id")
    private String studentId; // 3、学生证号

    @Column(name = "register_date")
    private LocalDate registerDate; // 4、入学时间

    @Column(name = "citizen_id")
    private String citizenId; // 5、身份证号

    @Column(name = "gender")
    private String gender; // 6、性别

    @Column(name = "current_age")
    private Integer currentAge; // 7、当前年龄

    @Column(name = "cell_phone_number")
    private String cellPhoneNumber; // 8、手机号

    @Column(name = "native_place")
    private String nativePlace; // 9、籍贯

    @Column(name = "political_status")
    private String politicalStatus; // 10、政治面貌

    @Column(name = "undergraduate_school")
    private String undergraduateSchool; // 11、本科院校

    @Column(name = "undergraduate_major")
    private String undergraduateMajor; // 12、本科专业

    @Column(name = "enrollment_type")
    private String enrollmentType; // 13、入学方式

    @Column(name = "postgraduate_score")
    private Integer postgraduateScore; // 14、考研成绩

    @Column(name = "school")
    private String school; // 15、所在学院

    @Column(name = "major")
    private String major; // 16、所在专业

    @Column(name = "supervisor_name")
    private String supervisorName; // 17、导师姓名

    @Column(name = "supervisor_work_id")
    private String supervisorWorkId; // 18、导师工作证号

    @Column(name = "counselor_name")
    private String counselorName; // 19、辅导员姓名

    @Column(name = "counselor_work_id")
    private String counselorWorkId; // 20、辅导员工作证号

    @Column(name = "current_grade")
    private String currentGrade; // 21、当前年级

    @Column(name = "is_student_leader")
    private Boolean isStudentLeader; // 22、学生干部

    @Column(name = "individual_award_count")
    private Integer individualAwardCount; // 23、个人奖励次数

    @Column(name = "improper_remarks_count")
    private Integer improperRemarksCount; // 24、不当言论次数

    @Column(name = "highest_punishment_level")
    private String highestPunishmentLevel; // 25、最高受惩级别

    @Column(name = "latest_scholarship_level")
    private String latestScholarshipLevel; // 26、学业奖学金级别

    @Column(name = "academic_index")
    private Double academicIndex; // 27、学业健康指数

    @Column(name = "prize_count")
    private Integer prizeCount; // 28、国奖获奖次数

    @Column(name = "competition_count")
    private Integer competitionCount; // 29、竞赛获奖次数

    @Column(name = "credit")
    private Double credit; // 30、当前学分进度

    @Column(name = "failed_credit")
    private Integer failedCredit; // 31、不及格学分

    @Column(name = "paper_count")
    private Integer paperCount; // 32、发表论文数

    @Column(name = "ei_paper_count")
    private Integer eiPaperCount; // 33、发表EI论文数

    @Column(name = "sci_paper_count")
    private Integer sciPaperCount; // 34、发表SCI论文数

    @Column(name = "academic_status")
    private String academicStatus; // 35、学业状态

    @Column(name = "graduation_design_index")
    private Double graduationDesignIndex; // 36、毕设健康指数

    @Column(name = "thesis_proposal")
    private String thesisProposal; // 37、开题情况

    @Column(name = "middle_check")
    private String middleCheck; // 38、中期检查

    @Column(name = "duplicate_check")
    private String duplicateCheck; // 39、查重

    @Column(name = "blind_review")
    private String blindReview; // 40、盲审

    @Column(name = "pre_defense")
    private String preDefense; // 41、预答辩

    @Column(name = "defense")
    private String defense; // 42、答辩

    @Column(name = "employment_index")
    private Double employmentIndex; // 43、就业健康指数

    @Column(name = "job_intention")
    private String jobIntention; // 44、主观求职意向

    @Column(name = "sign")
    private Boolean sign; // 45、签约

    @Column(name = "offer")
    private Boolean offer; // 46、意向

    @Column(name = "admission")
    private Boolean admission; // 47、录取

    @Column(name = "living_index")
    private Double livingIndex; // 48、生活健康指数

    @Column(name = "average_fee")
    private Double averageFee; // 49、周平均消费

    @Column(name = "std_fee")
    private Double stdFee; // 50、消费标准差

    @Column(name = "gradient_fee")
    private Double gradientFee; // 51、消费梯度

    @Column(name = "average_food_fee")
    private Double averageFoodFee; // 52、周平均食堂消费金额

    @Column(name = "std_food_fee")
    private Double stdFoodFee; // 53、食堂消费金额标准差

    @Column(name = "gradient_food_fee")
    private Double gradientFoodFee; // 54、食堂消费金额梯度

    @Column(name = "average_food_count")
    private Double averageFoodCount; // 55、周平均食堂消费次数

    @Column(name = "std_food_count")
    private Double stdFoodCount; // 56、食堂消费次数标准差

    @Column(name = "gradient_food_count")
    private Double gradientFoodCount; // 57、食堂消费次数梯度

    @Column(name = "average_bath_fee")
    private Double averageBathFee; // 58、周平均浴室消费金额

    @Column(name = "std_bath_fee")
    private Double stdBathFee; // 59、浴室消费金额标准差

    @Column(name = "gradient_bath_fee")
    private Double gradientBathFee; // 60、浴室消费金额梯度

    @Column(name = "average_bath_count")
    private Double averageBathCount; // 61、周平均浴室消费次数

    @Column(name = "std_bath_count")
    private Double stdBathCount; // 62、浴室消费次数标准差

    @Column(name = "gradient_bath_count")
    private Double gradientBathCount; // 63、浴室消费次数梯度

    @Column(name = "average_store_fee")
    private Double averageStoreFee; // 64、周平均超市消费金额

    @Column(name = "std_store_fee")
    private Double stdStoreFee; // 65、超市消费金额标准差

    @Column(name = "gradient_store_fee")
    private Double gradientStoreFee; // 66、超市消费金额梯度

    @Column(name = "average_store_count")
    private Double averageStoreCount; // 67、周平均超市消费次数

    @Column(name = "std_store_count")
    private Double stdStoreCount; // 68、超市消费次数标准差

    @Column(name = "gradient_store_count")
    private Double gradientStoreCount; // 69、超市消费次数梯度

    @Column(name = "average_stadium_count")
    private Double averageStadiumCount; // 70、周平均运动场进入次数

    @Column(name = "std_stadium_count")
    private Double stdStadiumCount; // 71、运动场进入次数标准差

    @Column(name = "gradient_stadium_count")
    private Double gradientStadiumCount; // 72、运动场进入次数梯度

    @Column(name = "average_exit_count")
    private Double averageExitCount; // 73、周平均出校门次数

    @Column(name = "std_exit_count")
    private Double stdExitCount; // 74、出校门次数标准差

    @Column(name = "gradient_exit_count")
    private Double gradientExitCount; // 75、出校门次数梯度

    @Column(name = "average_stay_hour")
    private Double averageStayHour; // 76、周平均宿舍驻留时长

    @Column(name = "std_stay_hour")
    private Double stdStayHour; // 77、宿舍驻留时长标准差

    @Column(name = "gradient_stay_hour")
    private Double gradientStayHour; // 78、宿舍驻留时长梯度

    @Column(name = "average_internet_hour")
    private Double averageInternetHour; // 79、周平均上网时长

    @Column(name = "std_internet_hour")
    private Double stdInternetHour; // 80、上网时长标准差

    @Column(name = "gradient_internet_hour")
    private Double gradientInternetHour; // 81、上网时长梯度

    @Column(name = "average_mobile_hour")
    private Double averageMobileHour; // 82、周平均移动端上网时长

    @Column(name = "std_mobile_hour")
    private Double stdMobileHour; // 83、移动端上网时长标准差

    @Column(name = "gradient_mobile_hour")
    private Double gradientMobileHour; // 84、移动端上网时长梯度

    @Column(name = "average_pc_hour")
    private Double averagePcHour; // 85、周平均非移动端上网时长

    @Column(name = "std_pc_hour")
    private Double stdPcHour; // 86、非移动端上网时长标准差

    @Column(name = "gradient_pc_hour")
    private Double gradientPcHour; // 87、非移动端上网时长梯度

    @Column(name = "average_forum_post")
    private Double averageForumPost; // 88、周平均论坛发帖次数

    @Column(name = "std_forum_post")
    private Double stdForumPost; // 89、论坛发帖次数标准差

    @Column(name = "gradient_forum_post")
    private Double gradientForumPost; // 90、论坛发帖次数梯度

    @Column(name = "average_forum_reply")
    private Double averageForumReply; // 91、周平均论坛回帖次数

    @Column(name = "std_forum_reply")
    private Double stdForumReply; // 92、论坛回帖次数标准差

    @Column(name = "gradient_forum_reply")
    private Double gradientForumReply; // 93、论坛回帖次数梯度

    @Column(name = "average_late_night_post")
    private Double averageLateNightPost; // 94、周平均论坛深夜发帖次数

    @Column(name = "std_late_night_post")
    private Double stdLateNightPost; // 95、论坛深夜发帖次数标准差

    @Column(name = "gradient_late_night_post")
    private Double gradientLateNightPost; // 96、论坛深夜发帖次数梯度

    @Column(name = "average_late_night_reply")
    private Double averageLateNightReply; // 97、周平均论坛深夜回帖次数

    @Column(name = "std_late_night_reply")
    private Double stdLateNightReply; // 98、论坛深夜回帖次数标准差

    @Column(name = "gradient_late_night_reply")
    private Double gradientLateNightReply; // 99、论坛深夜回帖次数梯度

    @Column(name = "monthly_abnormal_count")
    private Integer monthlyAbnormalCount; // 100、月异常行为反馈次数

    @Column(name = "latest_issue_type")
    private String latestIssueType; // 101、问题种类

    @Column(name = "is_only_child")
    private Boolean isOnlyChild; // 102、独生子女

    @Column(name = "is_poverty_student")
    private Boolean isPovertyStudent; // 103、经济困难生

    @Column(name = "flag")
    private Integer flag; // 104、标注
}