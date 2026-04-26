package com.bupt.middleware.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenxiao
 * @date 2025/4/5 21:09
        * @description: 宽表属性
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PgWideTableAttributes {
    @JsonProperty(value = "sn")
    private Boolean sn; // 1、序列号

    @JsonProperty(value = "graduateName")
    private Boolean graduateName; // 2、姓名

    @JsonProperty(value = "studentId")
    private Boolean studentId; // 3、学生证号

    @JsonProperty(value = "registerDate")
    private Boolean registerDate; // 4、入学时间

    @JsonProperty(value = "citizenId")
    private Boolean citizenId; // 5、身份证号

    @JsonProperty(value = "gender")
    private Boolean gender; // 6、性别

    @JsonProperty(value = "currentAge")
    private Boolean currentAge; // 7、当前年龄

    @JsonProperty(value = "cellPhoneNumber")
    private Boolean cellPhoneNumber; // 8、手机号

    @JsonProperty(value = "nativePlace")
    private Boolean nativePlace; // 9、籍贯

    @JsonProperty(value = "politicalStatus")
    private Boolean politicalStatus; // 10、政治面貌

    @JsonProperty(value = "undergraduateSchool")
    private Boolean undergraduateSchool; // 11、本科院校

    @JsonProperty(value = "undergraduateMajor")
    private Boolean undergraduateMajor; // 12、本科专业

    @JsonProperty(value = "enrollmentType")
    private Boolean enrollmentType; // 13、入学方式

    @JsonProperty(value = "postgraduateScore")
    private Boolean postgraduateScore; // 14、考研成绩

    @JsonProperty(value = "school")
    private Boolean school; // 15、所在学院

    @JsonProperty(value = "major")
    private Boolean major; // 16、所在专业

    @JsonProperty(value = "supervisorName")
    private Boolean supervisorName; // 17、导师姓名

    @JsonProperty(value = "supervisorWorkId")
    private Boolean supervisorWorkId; // 18、导师工作证号

    @JsonProperty(value = "counselorName")
    private Boolean counselorName; // 19、辅导员姓名

    @JsonProperty(value = "counselorWorkId")
    private Boolean counselorWorkId; // 20、辅导员工作证号

    @JsonProperty(value = "currentGrade")
    private Boolean currentGrade; // 21、当前年级

    @JsonProperty(value = "isStudentLeader")
    private Boolean isStudentLeader; // 22、学生干部

    @JsonProperty(value = "individualAwardCount")
    private Boolean individualAwardCount; // 23、个人奖励次数

    @JsonProperty(value = "improperRemarksCount")
    private Boolean improperRemarksCount; // 24、不当言论次数

    @JsonProperty(value = "highestPunishmentLevel")
    private Boolean highestPunishmentLevel; // 25、最高受惩级别

    @JsonProperty(value = "latestScholarshipLevel")
    private Boolean latestScholarshipLevel; // 26、学业奖学金级别

    @JsonProperty(value = "academicIndex")
    private Boolean academicIndex; // 27、学业健康指数

    @JsonProperty(value = "prizeCount")
    private Boolean prizeCount; // 28、国奖获奖次数

    @JsonProperty(value = "competitionCount")
    private Boolean competitionCount; // 29、竞赛获奖次数

    @JsonProperty(value = "credit")
    private Boolean credit; // 30、当前学分进度

    @JsonProperty(value = "failedCredit")
    private Boolean failedCredit; // 31、不及格学分

    @JsonProperty(value = "paperCount")
    private Boolean paperCount; // 32、发表论文数

    @JsonProperty(value = "eiPaperCount")
    private Boolean eiPaperCount; // 33、发表EI论文数

    @JsonProperty(value = "sciPaperCount")
    private Boolean sciPaperCount; // 34、发表SCI论文数

    @JsonProperty(value = "academicStatus")
    private Boolean academicStatus; // 35、学业状态

    @JsonProperty(value = "graduationDesignIndex")
    private Boolean graduationDesignIndex; // 36、毕设健康指数

    @JsonProperty(value = "thesisProposal")
    private Boolean thesisProposal; // 37、开题情况

    @JsonProperty(value = "middleCheck")
    private Boolean middleCheck; // 38、中期检查

    @JsonProperty(value = "duplicateCheck")
    private Boolean duplicateCheck; // 39、查重

    @JsonProperty(value = "blindReview")
    private Boolean blindReview; // 40、盲审

    @JsonProperty(value = "preDefense")
    private Boolean preDefense; // 41、预答辩

    @JsonProperty(value = "defense")
    private Boolean defense; // 42、答辩

    @JsonProperty(value = "employmentIndex")
    private Boolean employmentIndex; // 43、就业健康指数

    @JsonProperty(value = "jobIntention")
    private Boolean jobIntention; // 44、主观求职意向

    @JsonProperty(value = "sign")
    private Boolean sign; // 45、签约

    @JsonProperty(value = "offer")
    private Boolean offer; // 46、意向

    @JsonProperty(value = "admission")
    private Boolean admission; // 47、录取

    @JsonProperty(value = "livingIndex")
    private Boolean livingIndex; // 48、生活健康指数

    @JsonProperty(value = "averageFee")
    private Boolean averageFee; // 49、周平均消费

    @JsonProperty(value = "stdFee")
    private Boolean stdFee; // 50、消费标准差

    @JsonProperty(value = "gradientFee")
    private Boolean gradientFee; // 51、消费梯度

    @JsonProperty(value = "averageFoodFee")
    private Boolean averageFoodFee; // 52、周平均食堂消费金额

    @JsonProperty(value = "stdFoodFee")
    private Boolean stdFoodFee; // 53、食堂消费金额标准差

    @JsonProperty(value = "gradientFoodFee")
    private Boolean gradientFoodFee; // 54、食堂消费金额梯度

    @JsonProperty(value = "averageFoodCount")
    private Boolean averageFoodCount; // 55、周平均食堂消费次数

    @JsonProperty(value = "stdFoodCount")
    private Boolean stdFoodCount; // 56、食堂消费次数标准差

    @JsonProperty(value = "gradientFoodCount")
    private Boolean gradientFoodCount; // 57、食堂消费次数梯度

    @JsonProperty(value = "averageBathFee")
    private Boolean averageBathFee; // 58、周平均浴室消费金额

    @JsonProperty(value = "stdBathFee")
    private Boolean stdBathFee; // 59、浴室消费金额标准差

    @JsonProperty(value = "gradientBathFee")
    private Boolean gradientBathFee; // 60、浴室消费金额梯度

    @JsonProperty(value = "averageBathCount")
    private Boolean averageBathCount; // 61、周平均浴室消费次数

    @JsonProperty(value = "stdBathCount")
    private Boolean stdBathCount; // 62、浴室消费次数标准差

    @JsonProperty(value = "gradientBathCount")
    private Boolean gradientBathCount; // 63、浴室消费次数梯度

    @JsonProperty(value = "averageStoreFee")
    private Boolean averageStoreFee; // 64、周平均超市消费金额

    @JsonProperty(value = "stdStoreFee")
    private Boolean stdStoreFee; // 65、超市消费金额标准差

    @JsonProperty(value = "gradientStoreFee")
    private Boolean gradientStoreFee; // 66、超市消费金额梯度

    @JsonProperty(value = "averageStoreCount")
    private Boolean averageStoreCount; // 67、周平均超市消费次数

    @JsonProperty(value = "stdStoreCount")
    private Boolean stdStoreCount; // 68、超市消费次数标准差

    @JsonProperty(value = "gradientStoreCount")
    private Boolean gradientStoreCount; // 69、超市消费次数梯度

    @JsonProperty(value = "averageStadiumCount")
    private Boolean averageStadiumCount; // 70、周平均运动场进入次数

    @JsonProperty(value = "stdStadiumCount")
    private Boolean stdStadiumCount; // 71、运动场进入次数标准差

    @JsonProperty(value = "gradientStadiumCount")
    private Boolean gradientStadiumCount; // 72、运动场进入次数梯度

    @JsonProperty(value = "averageExitCount")
    private Boolean averageExitCount; // 73、周平均出校门次数

    @JsonProperty(value = "stdExitCount")
    private Boolean stdExitCount; // 74、出校门次数标准差

    @JsonProperty(value = "gradientExitCount")
    private Boolean gradientExitCount; // 75、出校门次数梯度

    @JsonProperty(value = "averageStayHour")
    private Boolean averageStayHour; // 76、周平均宿舍驻留时长

    @JsonProperty(value = "stdStayHour")
    private Boolean stdStayHour; // 77、宿舍驻留时长标准差

    @JsonProperty(value = "gradientStayHour")
    private Boolean gradientStayHour; // 78、宿舍驻留时长梯度

    @JsonProperty(value = "averageInternetHour")
    private Boolean averageInternetHour; // 79、周平均上网时长

    @JsonProperty(value = "stdInternetHour")
    private Boolean stdInternetHour; // 80、上网时长标准差

    @JsonProperty(value = "gradientInternetHour")
    private Boolean gradientInternetHour; // 81、上网时长梯度

    @JsonProperty(value = "averageMobileHour")
    private Boolean averageMobileHour; // 82、周平均移动端上网时长

    @JsonProperty(value = "stdMobileHour")
    private Boolean stdMobileHour; // 83、移动端上网时长标准差

    @JsonProperty(value = "gradientMobileHour")
    private Boolean gradientMobileHour; // 84、移动端上网时长梯度

    @JsonProperty(value = "averagePcHour")
    private Boolean averagePcHour; // 85、周平均非移动端上网时长

    @JsonProperty(value = "stdPcHour")
    private Boolean stdPcHour; // 86、非移动端上网时长标准差

    @JsonProperty(value = "gradientPcHour")
    private Boolean gradientPcHour; // 87、非移动端上网时长梯度

    @JsonProperty(value = "averageForumPost")
    private Boolean averageForumPost; // 88、周平均论坛发帖次数

    @JsonProperty(value = "stdForumPost")
    private Boolean stdForumPost; // 89、论坛发帖次数标准差

    @JsonProperty(value = "gradientForumPost")
    private Boolean gradientForumPost; // 90、论坛发帖次数梯度

    @JsonProperty(value = "averageForumReply")
    private Boolean averageForumReply; // 91、周平均论坛回帖次数

    @JsonProperty(value = "stdForumReply")
    private Boolean stdForumReply; // 92、论坛回帖次数标准差

    @JsonProperty(value = "gradientForumReply")
    private Boolean gradientForumReply; // 93、论坛回帖次数梯度

    @JsonProperty(value = "averageLateNightPost")
    private Boolean averageLateNightPost; // 94、周平均论坛深夜发帖次数

    @JsonProperty(value = "stdLateNightPost")
    private Boolean stdLateNightPost; // 95、论坛深夜发帖次数标准差

    @JsonProperty(value = "gradientLateNightPost")
    private Boolean gradientLateNightPost; // 96、论坛深夜发帖次数梯度

    @JsonProperty(value = "averageLateNightReply")
    private Boolean averageLateNightReply; // 97、周平均论坛深夜回帖次数

    @JsonProperty(value = "stdLateNightReply")
    private Boolean stdLateNightReply; // 98、论坛深夜回帖次数标准差

    @JsonProperty(value = "gradientLateNightReply")
    private Boolean gradientLateNightReply; // 99、论坛深夜回帖次数梯度

    @JsonProperty(value = "monthlyAbnormalCount")
    private Boolean monthlyAbnormalCount; // 10、月异常行为反馈次数

    @JsonProperty(value = "latestIssueType")
    private Boolean latestIssueType; // 101、问题种类

    @JsonProperty(value = "isOnlyChild")
    private Boolean isOnlyChild; // 102、独生子女

    @JsonProperty(value = "isPovertyStudent")
    private Boolean isPovertyStudent; // 103、经济困难生

}