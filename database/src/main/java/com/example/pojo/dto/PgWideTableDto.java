package com.example.pojo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PgWideTableDto {

    @NotBlank(message = "序列号不能为空")
    private String sn; // 1、序列号

    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String graduateName; // 2、姓名

    @NotBlank(message = "学生证号不能为空")
    @Pattern(regexp = "^\\d{10}$", message = "学生证号必须是10位数字")
    private String studentId; // 3、学生证号

    @NotNull(message = "入学时间不能为空")
    @PastOrPresent(message = "入学时间必须是过去或现在的时间")
    private LocalDate registerDate; // 4、入学时间

    @NotBlank(message = "身份证号不能为空")
    private String citizenId; // 5、身份证号

    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^[男女]$", message = "性别必须是'男'或'女'")
    private String gender; // 6、性别

    @NotNull(message = "当前年龄不能为空")
    @Min(value = 18, message = "年龄最小为18岁")
    @Max(value = 50, message = "年龄最大为50岁")
    private Integer currentAge; // 7、当前年龄

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String cellPhoneNumber; // 8、手机号

    @NotBlank(message = "籍贯不能为空")
    @Size(max = 100, message = "籍贯长度不能超过100个字符")
    private String nativePlace; // 9、籍贯

    @NotBlank(message = "政治面貌不能为空")
    @Size(max = 20, message = "政治面貌长度不能超过20个字符")
    private String politicalStatus; // 10、政治面貌

    @NotBlank(message = "本科院校不能为空")
    @Size(max = 100, message = "本科院校长度不能超过100个字符")
    private String undergraduateSchool; // 11、本科院校

    @NotBlank(message = "本科专业不能为空")
    @Size(max = 50, message = "本科专业长度不能超过50个字符")
    private String undergraduateMajor; // 12、本科专业

    @NotBlank(message = "入学方式不能为空")
    @Size(max = 20, message = "入学方式长度不能超过20个字符")
    private String enrollmentType; // 13、入学方式

    @NotNull(message = "考研成绩不能为空")
    @Max(value = 500, message = "考研成绩最大为500")
    private Integer postgraduateScore; // 14、考研成绩

    @NotBlank(message = "所在学院不能为空")
    @Size(max = 50, message = "所在学院长度不能超过50个字符")
    private String school; // 15、所在学院

    @NotBlank(message = "所在专业不能为空")
    @Size(max = 50, message = "所在专业长度不能超过50个字符")
    private String major; // 16、所在专业

    @NotBlank(message = "导师姓名不能为空")
    @Size(max = 50, message = "导师姓名长度不能超过50个字符")
    private String supervisorName; // 17、导师姓名

    @NotBlank(message = "导师工作证号不能为空")
    @Pattern(regexp = "^\\d{10}$", message = "导师工作证号必须是10位数字")
    private String supervisorWorkId; // 18、导师工作证号

    @NotBlank(message = "辅导员姓名不能为空")
    @Size(max = 50, message = "辅导员姓名长度不能超过50个字符")
    private String counselorName; // 19、辅导员姓名

    @NotBlank(message = "辅导员工作证号不能为空")
    @Pattern(regexp = "^\\d{10}$", message = "辅导员工作证号必须是10位数字")
    private String counselorWorkId; // 20、辅导员工作证号

    @NotBlank(message = "当前年级不能为空")
    @Size(max = 20, message = "当前年级长度不能超过20个字符")
    private String currentGrade; // 21、当前年级

    @NotNull(message = "是否学生干部不能为空")
    private Boolean isStudentLeader; // 22、学生干部

    @NotNull(message = "个人奖励次数不能为空")
    @Min(value = 0, message = "个人奖励次数最小为0")
    private Integer individualAwardCount; // 23、个人奖励次数

    @NotNull(message = "不当言论次数不能为空")
    @Min(value = 0, message = "不当言论次数最小为0")
    private Integer improperRemarksCount; // 24、不当言论次数

    @Size(max = 20, message = "最高受惩级别长度不能超过20个字符")
    private String highestPunishmentLevel; // 25、最高受惩级别

    @Size(max = 20, message = "学业奖学金级别长度不能超过20个字符")
    private String latestScholarshipLevel; // 26、学业奖学金级别

    @DecimalMin(value = "0.0", message = "学业健康指数最小为0")
    @DecimalMax(value = "1.0", message = "学业健康指数最大为1")
    private Double academicIndex; // 27、学业健康指数

    @NotNull(message = "国奖获奖次数不能为空")
    @Min(value = 0, message = "国奖获奖次数最小为0")
    private Integer prizeCount; // 28、国奖获奖次数

    @NotNull(message = "竞赛获奖次数不能为空")
    @Min(value = 0, message = "竞赛获奖次数最小为0")
    private Integer competitionCount; // 29、竞赛获奖次数

    @NotNull(message = "当前学分进度不能为空")
    @DecimalMin(value = "0.0", message = "当前学分进度最小为0")
    @DecimalMax(value = "1.0", message = "当前学分进度最大为1")
    private Double credit; // 30、当前学分进度

    @NotNull(message = "不及格学分不能为空")
    @Min(value = 0, message = "不及格学分最小为0")
    private Integer failedCredit; // 31、不及格学分

    @NotNull(message = "发表论文数不能为空")
    @Min(value = 0, message = "发表论文数最小为0")
    private Integer paperCount; // 32、发表论文数

    @NotNull(message = "发表EI论文数不能为空")
    @Min(value = 0, message = "发表EI论文数最小为0")
    private Integer eiPaperCount; // 33、发表EI论文数

    @NotNull(message = "发表SCI论文数不能为空")
    @Min(value = 0, message = "发表SCI论文数最小为0")
    private Integer sciPaperCount; // 34、发表SCI论文数

    @Size(max = 20, message = "学业状态长度不能超过20个字符")
    private String academicStatus; // 35、学业状态

    @DecimalMin(value = "0.0", message = "毕设健康指数最小为0")
    @DecimalMax(value = "1.0", message = "毕设健康指数最大为1")
    private Double graduationDesignIndex; // 36、毕设健康指数

    @Size(max = 20, message = "开题情况长度不能超过20个字符")
    private String thesisProposal; // 37、开题情况

    @Size(max = 20, message = "中期检查长度不能超过20个字符")
    private String middleCheck; // 38、中期检查

    @Size(max = 20, message = "查重长度不能超过20个字符")
    private String duplicateCheck; // 39、查重

    @Size(max = 20, message = "盲审长度不能超过20个字符")
    private String blindReview; // 40、盲审

    @Size(max = 20, message = "预答辩长度不能超过20个字符")
    private String preDefense; // 41、预答辩

    @Size(max = 20, message = "答辩长度不能超过20个字符")
    private String defense; // 42、答辩

    @DecimalMin(value = "0.0", message = "就业健康指数最小为0")
    @DecimalMax(value = "1.0", message = "就业健康指数最大为1")
    private Double employmentIndex; // 43、就业健康指数

    @Size(max = 50, message = "主观求职意向长度不能超过50个字符")
    private String jobIntention; // 44、主观求职意向

    @NotNull(message = "是否签约不能为空")
    private Boolean sign; // 45、签约

    @NotNull(message = "是否有意向不能为空")
    private Boolean offer; // 46、意向

    @NotNull(message = "是否录取不能为空")
    private Boolean admission; // 47、录取

    @DecimalMin(value = "0.0", message = "生活健康指数最小为0")
    @DecimalMax(value = "1.0", message = "生活健康指数最大为1")
    private Double livingIndex; // 48、生活健康指数

    @DecimalMin(value = "0.0", message = "周平均消费最小为0")
    private Double averageFee; // 49、周平均消费

    @DecimalMin(value = "0.0", message = "消费标准差最小为0")
    private Double stdFee; // 50、消费标准差

    private Double gradientFee; // 51、消费梯度

    @DecimalMin(value = "0.0", message = "周平均食堂消费金额最小为0")
    private Double averageFoodFee; // 52、周平均食堂消费金额

    @DecimalMin(value = "0.0", message = "食堂消费金额标准差最小为0")
    private Double stdFoodFee; // 53、食堂消费金额标准差

    private Double gradientFoodFee; // 54、食堂消费金额梯度

    @DecimalMin(value = "0.0", message = "周平均食堂消费次数最小为0")
    private Double averageFoodCount; // 55、周平均食堂消费次数

    @DecimalMin(value = "0.0", message = "食堂消费次数标准差最小为0")
    private Double stdFoodCount; // 56、食堂消费次数标准差

    private Double gradientFoodCount; // 57、食堂消费次数梯度

    @DecimalMin(value = "0.0", message = "周平均浴室消费金额最小为0")
    private Double averageBathFee; // 58、周平均浴室消费金额

    @DecimalMin(value = "0.0", message = "浴室消费金额标准差最小为0")
    private Double stdBathFee; // 59、浴室消费金额标准差

    private Double gradientBathFee; // 60、浴室消费金额梯度

    @DecimalMin(value = "0.0", message = "周平均浴室消费次数最小为0")
    private Double averageBathCount; // 61、周平均浴室消费次数

    @DecimalMin(value = "0.0", message = "浴室消费次数标准差最小为0")
    private Double stdBathCount; // 62、浴室消费次数标准差

    private Double gradientBathCount; // 63、浴室消费次数梯度

    @DecimalMin(value = "0.0", message = "周平均超市消费金额最小为0")
    private Double averageStoreFee; // 64、周平均超市消费金额

    @DecimalMin(value = "0.0", message = "超市消费金额标准差最小为0")
    private Double stdStoreFee; // 65、超市消费金额标准差

    private Double gradientStoreFee; // 66、超市消费金额梯度

    @DecimalMin(value = "0.0", message = "周平均超市消费次数最小为0")
    private Double averageStoreCount; // 67、周平均超市消费次数

    @DecimalMin(value = "0.0", message = "超市消费次数标准差最小为0")
    private Double stdStoreCount; // 68、超市消费次数标准差

    private Double gradientStoreCount; // 69、超市消费次数梯度

    @DecimalMin(value = "0.0", message = "周平均运动场进入次数最小为0")
    private Double averageStadiumCount; // 70、周平均运动场进入次数

    @DecimalMin(value = "0.0", message = "运动场进入次数标准差最小为0")
    private Double stdStadiumCount; // 71、运动场进入次数标准差

    private Double gradientStadiumCount; // 72、运动场进入次数梯度

    @DecimalMin(value = "0.0", message = "周平均出校门次数最小为0")
    private Double averageExitCount; // 73、周平均出校门次数

    @DecimalMin(value = "0.0", message = "出校门次数标准差最小为0")
    private Double stdExitCount; // 74、出校门次数标准差

    private Double gradientExitCount; // 75、出校门次数梯度

    @DecimalMin(value = "0.0", message = "周平均宿舍驻留时长最小为0")
    private Double averageStayHour; // 76、周平均宿舍驻留时长

    @DecimalMin(value = "0.0", message = "宿舍驻留时长标准差最小为0")
    private Double stdStayHour; // 77、宿舍驻留时长标准差

    private Double gradientStayHour; // 78、宿舍驻留时长梯度

    @DecimalMin(value = "0.0", message = "周平均上网时长最小为0")
    private Double averageInternetHour; // 79、周平均上网时长

    @DecimalMin(value = "0.0", message = "上网时长标准差最小为0")
    private Double stdInternetHour; // 80、上网时长标准差

    private Double gradientInternetHour; // 81、上网时长梯度

    @DecimalMin(value = "0.0", message = "周平均移动端上网时长最小为0")
    private Double averageMobileHour; // 82、周平均移动端上网时长

    @DecimalMin(value = "0.0", message = "移动端上网时长标准差最小为0")
    private Double stdMobileHour; // 83、移动端上网时长标准差

    private Double gradientMobileHour; // 84、移动端上网时长梯度

    @DecimalMin(value = "0.0", message = "周平均非移动端上网时长最小为0")
    private Double averagePcHour; // 85、周平均非移动端上网时长

    @DecimalMin(value = "0.0", message = "非移动端上网时长标准差最小为0")
    private Double stdPcHour; // 86、非移动端上网时长标准差

    private Double gradientPcHour; // 87、非移动端上网时长梯度

    @DecimalMin(value = "0.0", message = "周平均论坛发帖次数最小为0")
    private Double averageForumPost; // 88、周平均论坛发帖次数

    @DecimalMin(value = "0.0", message = "论坛发帖次数标准差最小为0")
    private Double stdForumPost; // 89、论坛发帖次数标准差

    private Double gradientForumPost; // 90、论坛发帖次数梯度

    @DecimalMin(value = "0.0", message = "周平均论坛回帖次数最小为0")
    private Double averageForumReply; // 91、周平均论坛回帖次数

    @DecimalMin(value = "0.0", message = "论坛回帖次数标准差最小为0")
    private Double stdForumReply; // 92、论坛回帖次数标准差

    private Double gradientForumReply; // 93、论坛回帖次数梯度

    @DecimalMin(value = "0.0", message = "周平均论坛深夜发帖次数最小为0")
    private Double averageLateNightPost; // 94、周平均论坛深夜发帖次数

    @DecimalMin(value = "0.0", message = "论坛深夜发帖次数标准差最小为0")
    private Double stdLateNightPost; // 95、论坛深夜发帖次数标准差

    private Double gradientLateNightPost; // 96、论坛深夜发帖次数梯度

    @DecimalMin(value = "0.0", message = "周平均论坛深夜回帖次数最小为0")
    private Double averageLateNightReply; // 97、周平均论坛深夜回帖次数

    @DecimalMin(value = "0.0", message = "论坛深夜回帖次数标准差最小为0")
    private Double stdLateNightReply; // 98、论坛深夜回帖次数标准差

    private Double gradientLateNightReply; // 99、论坛深夜回帖次数梯度

    @Min(value = 0, message = "月异常行为反馈次数最小为0")
    private Integer monthlyAbnormalCount; // 100、月异常行为反馈次数

    @Size(max = 50, message = "问题种类长度不能超过50个字符")
    private String latestIssueType; // 101、问题种类

    @NotNull(message = "是否独生子女不能为空")
    private Boolean isOnlyChild; // 102、独生子女

    @NotNull(message = "是否经济困难生不能为空")
    private Boolean isPovertyStudent; // 103、经济困难生

    private Integer flag; // 104、标注
}