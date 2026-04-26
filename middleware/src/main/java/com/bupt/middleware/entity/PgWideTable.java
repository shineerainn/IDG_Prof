package com.bupt.middleware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author chenxiao
 * @date 2025/4/7 12:46
 * @description: Postgraduate Wide Table Entity Class
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PgWideTable {

    private String sn; // 1、序列号

    private String graduateName; // 2、姓名

    private String studentId; // 3、学生证号

    private LocalDate registerDate; // 4、入学时间

    private String citizenId; // 5、身份证号

    private String gender; // 6、性别

    private Integer currentAge; // 7、当前年龄

    private String cellPhoneNumber; // 8、手机号

    private String nativePlace; // 9、籍贯

    private String politicalStatus; // 10、政治面貌

    private String undergraduateSchool; // 11、本科院校

    private String undergraduateMajor; // 12、本科专业

    private String enrollmentType; // 13、入学方式

    private Integer postgraduateScore; // 14、考研成绩

    private String school; // 15、所在学院

    private String major; // 16、所在专业

    private String supervisorName; // 17、导师姓名

    private String supervisorWorkId; // 18、导师工作证号

    private String counselorName; // 19、辅导员姓名

    private String counselorWorkId; // 20、辅导员工作证号

    private String currentGrade; // 21、当前年级

    private Boolean isStudentLeader; // 22、学生干部

    private Integer individualAwardCount; // 23、个人奖励次数

    private Integer improperRemarksCount; // 24、不当言论次数

    private String highestPunishmentLevel; // 25、最高受惩级别

    private String latestScholarshipLevel; // 26、学业奖学金级别

    private Double academicIndex; // 27、学业健康指数

    private Integer prizeCount; // 28、国奖获奖次数

    private Integer competitionCount; // 29、竞赛获奖次数

    private Double credit; // 30、当前学分进度

    private Integer failedCredit; // 31、不及格学分

    private Integer paperCount; // 32、发表论文数

    private Integer eiPaperCount; // 33、发表EI论文数

    private Integer sciPaperCount; // 34、发表SCI论文数

    private String academicStatus; // 35、学业状态

    private Double graduationDesignIndex; // 36、毕设健康指数

    private String thesisProposal; // 37、开题情况

    private String middleCheck; // 38、中期检查

    private String duplicateCheck; // 39、查重

    private String blindReview; // 40、盲审

    private String preDefense; // 41、预答辩

    private String defense; // 42、答辩

    private Double employmentIndex; // 43、就业健康指数

    private String jobIntention; // 44、主观求职意向

    private Boolean sign; // 45、签约

    private Boolean offer; // 46、意向

    private Boolean admission; // 47、录取

    private Double livingIndex; // 48、生活健康指数

    private Double averageFee; // 49、周平均消费

    private Double stdFee; // 50、消费标准差

    private Double gradientFee; // 51、消费梯度

    private Double averageFoodFee; // 52、周平均食堂消费金额

    private Double stdFoodFee; // 53、食堂消费金额标准差

    private Double gradientFoodFee; // 54、食堂消费金额梯度

    private Double averageFoodCount; // 55、周平均食堂消费次数

    private Double stdFoodCount; // 56、食堂消费次数标准差

    private Double gradientFoodCount; // 57、食堂消费次数梯度

    private Double averageBathFee; // 58、周平均浴室消费金额

    private Double stdBathFee; // 59、浴室消费金额标准差

    private Double gradientBathFee; // 60、浴室消费金额梯度

    private Double averageBathCount; // 61、周平均浴室消费次数

    private Double stdBathCount; // 62、浴室消费次数标准差

    private Double gradientBathCount; // 63、浴室消费次数梯度

    private Double averageStoreFee; // 64、周平均超市消费金额

    private Double stdStoreFee; // 65、超市消费金额标准差

    private Double gradientStoreFee; // 66、超市消费金额梯度

    private Double averageStoreCount; // 67、周平均超市消费次数

    private Double stdStoreCount; // 68、超市消费次数标准差

    private Double gradientStoreCount; // 69、超市消费次数梯度

    private Double averageStadiumCount; // 70、周平均运动场进入次数

    private Double stdStadiumCount; // 71、运动场进入次数标准差

    private Double gradientStadiumCount; // 72、运动场进入次数梯度

    private Double averageExitCount; // 73、周平均出校门次数

    private Double stdExitCount; // 74、出校门次数标准差

    private Double gradientExitCount; // 75、出校门次数梯度

    private Double averageStayHour; // 76、周平均宿舍驻留时长

    private Double stdStayHour; // 77、宿舍驻留时长标准差

    private Double gradientStayHour; // 78、宿舍驻留时长梯度

    private Double averageInternetHour; // 79、周平均上网时长

    private Double stdInternetHour; // 80、上网时长标准差

    private Double gradientInternetHour; // 81、上网时长梯度

    private Double averageMobileHour; // 82、周平均移动端上网时长

    private Double stdMobileHour; // 83、移动端上网时长标准差

    private Double gradientMobileHour; // 84、移动端上网时长梯度

    private Double averagePcHour; // 85、周平均非移动端上网时长

    private Double stdPcHour; // 86、非移动端上网时长标准差

    private Double gradientPcHour; // 87、非移动端上网时长梯度

    private Double averageForumPost; // 88、周平均论坛发帖次数

    private Double stdForumPost; // 89、论坛发帖次数标准差

    private Double gradientForumPost; // 90、论坛发帖次数梯度

    private Double averageForumReply; // 91、周平均论坛回帖次数

    private Double stdForumReply; // 92、论坛回帖次数标准差

    private Double gradientForumReply; // 93、论坛回帖次数梯度

    private Double averageLateNightPost; // 94、周平均论坛深夜发帖次数

    private Double stdLateNightPost; // 95、论坛深夜发帖次数标准差

    private Double gradientLateNightPost; // 96、论坛深夜发帖次数梯度

    private Double averageLateNightReply; // 97、周平均论坛深夜回帖次数

    private Double stdLateNightReply; // 98、论坛深夜回帖次数标准差

    private Double gradientLateNightReply; // 99、论坛深夜回帖次数梯度

    private Integer monthlyAbnormalCount; // 100、月异常行为反馈次数

    private String latestIssueType; // 101、问题种类

    private Boolean isOnlyChild; // 102、独生子女

    private Boolean isPovertyStudent; // 103、经济困难生

    private Integer flag; // 104、标注
}