package com.example.pojo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PgAcademicDto {

    @NotBlank(message = "毕业生姓名不能为空")
    @Size(max = 50, message = "毕业生姓名长度不能超过50个字符")
    private String graduateName; // 毕业生姓名

    @NotBlank(message = "学生证号不能为空")
    @Pattern(regexp = "^\\d{10}$", message = "学生证号必须是10位数字")
    private String studentId; // 学生证号

    @NotNull(message = "加权平均成绩不能为空")
    @DecimalMin(value = "0.0", message = "加权平均成绩不能小于0")
    @DecimalMax(value = "4.0", message = "加权平均成绩不能大于4")
    private Double weightedAvgGrade; // 加权平均成绩

    @NotNull(message = "核心课程成绩不能为空")
    @DecimalMin(value = "0.0", message = "核心课程成绩不能小于0")
    @DecimalMax(value = "4.0", message = "核心课程成绩不能大于4")
    private Double coreCourseGrade; // 核心课程成绩

    @NotNull(message = "上学期平均成绩不能为空")
    @DecimalMin(value = "0.0", message = "上学期平均成绩不能小于0")
    @DecimalMax(value = "4.0", message = "上学期平均成绩不能大于4")
    private Double lastSemesterAvgGrade; // 上学期平均成绩

    @NotNull(message = "GPA增长率不能为空")
    @DecimalMin(value = "0.0", message = "GPA增长率不能小于0")
    @DecimalMax(value = "4.0", message = "GPA增长率不能大于4")
    private Double gpaGrowthRate; // GPA增长率

    @NotNull(message = "成绩排名不能为空")
    @Min(value = 1, message = "成绩排名必须大于0")
    @Max(value = 10000, message = "成绩排名不能超过10000")
    private Integer gradeRanking; // 成绩排名

    @NotBlank(message = "排名等级不能为空")
    @Pattern(regexp = "^[ABCDEF]$", message = "排名等级必须是A、B、C、D、E、F中的一个")
    private String rankingLevel; // 排名等级

    @NotNull(message = "学业表现得分不能为空")
    @DecimalMin(value = "0.0", message = "学业表现得分不能小于0")
    @DecimalMax(value = "100.0", message = "学业表现得分不能大于100")
    private Double academicPerformanceScore; // 学业表现得分

    @NotBlank(message = "科研项目级别不能为空")
    @Pattern(regexp = "^(国家级|省部级|校级|横向)$", message = "科研项目级别必须是国家级、省部级、校级或横向")
    private String researchProjectLevel; // 科研项目级别

    @NotBlank(message = "项目角色不能为空")
    @Pattern(regexp = "^(负责人|核心成员|一般参与者|无)$", message = "项目角色必须是负责人、核心成员、一般参与者或无")
    private String projectRole; // 项目角色

    @NotBlank(message = "项目成果转化不能为空")
    @Pattern(regexp = "^(优秀课题|技术应用|无)$", message = "项目成果转化必须是优秀课题、技术应用或无")
    private String projectAchievementTransformation; // 项目成果转化

    @NotNull(message = "科研项目得分不能为空")
    @DecimalMin(value = "0.0", message = "科研项目得分不能小于0")
    @DecimalMax(value = "100.0", message = "科研项目得分不能大于100")
    private Double researchProjectScore; // 科研项目得分

    @NotNull(message = "论文答辩成绩不能为空")
    @Min(value = 0, message = "论文答辩成绩不能小于0")
    @Max(value = 100, message = "论文答辩成绩不能大于100")
    private Integer thesisDefenseGrade; // 论文答辩成绩

    @NotNull(message = "论文创新性不能为空")
    @Min(value = 0, message = "论文创新性评分不能小于0")
    @Max(value = 10, message = "论文创新性评分不能大于10")
    private Integer thesisInnovativeness; // 论文创新性

    @NotNull(message = "论文实用价值不能为空")
    @Min(value = 0, message = "论文实用价值评分不能小于0")
    @Max(value = 10, message = "论文实用价值评分不能大于10")
    private Integer thesisPracticalValue; // 论文实用价值

    @NotNull(message = "论文成果转化不能为空")
    @Min(value = 0, message = "论文成果转化评分不能小于0")
    @Max(value = 10, message = "论文成果转化评分不能大于10")
    private Integer thesisAchievementTransformation; // 论文成果转化

    @NotNull(message = "论文得分不能为空")
    @DecimalMin(value = "0.0", message = "论文得分不能小于0")
    @DecimalMax(value = "100.0", message = "论文得分不能大于100")
    private Double thesisScore; // 论文得分

    @NotBlank(message = "发表期刊会议不能为空")
    @Size(max = 200, message = "发表期刊会议长度不能超过200个字符")
    private String publishedJournalConference; // 发表期刊会议

    @NotBlank(message = "作者级别不能为空")
    private String authorLevel; // 作者级别

    @NotBlank(message = "是否高被引不能为空")
    @Pattern(regexp = "^(是|否|无)$", message = "是否高被引必须是是、否或无")
    private String isHighlyCited; // 是否高被引

    @NotBlank(message = "是否影响因子以上不能为空")
    @Pattern(regexp = "^(是|否|无)$", message = "是否影响因子以上必须是是、否或无")
    private String isIfAbove; // 是否影响因子以上

    @NotBlank(message = "是否封面论文不能为空")
    @Pattern(regexp = "^(是|否|无)$", message = "是否封面论文必须是是、否或无")
    private String isCoverPaper; // 是否封面论文

    @NotNull(message = "学术论文得分不能为空")
    @DecimalMin(value = "0.0", message = "学术论文得分不能小于0")
    @DecimalMax(value = "100.0", message = "学术论文得分不能大于100")
    private Double academicPaperScore; // 学术论文得分

    @NotBlank(message = "竞赛级别不能为空")
    private String competitionLevel; // 竞赛级别

    @NotBlank(message = "获奖级别不能为空")
    private String awardLevel; // 获奖级别

    @NotBlank(message = "竞赛范围不能为空")
    private String competitionScope; // 竞赛范围

    @NotBlank(message = "竞赛团队角色不能为空")
    private String competitionTeamRole; // 竞赛团队角色

    @NotNull(message = "竞赛得分不能为空")
    @DecimalMin(value = "0.0", message = "竞赛得分不能小于0")
    @DecimalMax(value = "100.0", message = "竞赛得分不能大于100")
    private Double competitionScore; // 竞赛得分

    @NotBlank(message = "专利软件类型不能为空")
    private String patentSoftwareType; // 专利软件类型

    @NotBlank(message = "法律状态系数不能为空")
    private String legalStatusCoefficient; // 法律状态系数

    @NotBlank(message = "技术转让金额不能为空")
    private String technologyTransferAmount; // 技术转让金额

    @NotNull(message = "专利软件得分不能为空")
    @DecimalMin(value = "0.0", message = "专利软件得分不能小于0")
    @DecimalMax(value = "100.0", message = "专利软件得分不能大于100")
    private Double patentSoftwareScore; // 专利软件得分

    @NotBlank(message = "是否有创业项目不能为空")
    private String hasStartupProject; // 是否有创业项目

    @NotBlank(message = "创业融资阶段不能为空")
    private String startupFundingStage; // 创业融资阶段

    @NotBlank(message = "是否注册公司不能为空")
    @Pattern(regexp = "^(是|否|无)$", message = "是否注册公司必须是是、否或无")
    private String isCompanyRegistered; // 是否注册公司

    @NotBlank(message = "是否有创业竞赛获奖不能为空")
    @Pattern(regexp = "^(是|否|无)$", message = "是否有创业竞赛获奖必须是是、否或无")
    private String hasStartupCompetitionAward; // 是否有创业竞赛获奖

    @NotNull(message = "技术应用企业数量不能为空")
    @Min(value = 0, message = "技术应用企业数量不能小于0")
    @Max(value = 1000, message = "技术应用企业数量不能超过1000")
    private Integer techApplicationEnterpriseCount; // 技术应用企业数量

    @NotBlank(message = "技术推广合同金额不能为空")
    private String techPromotionContractAmount; // 技术推广合同金额

    @NotNull(message = "其他创新成果得分不能为空")
    @DecimalMin(value = "0.0", message = "其他创新成果得分不能小于0")
    @DecimalMax(value = "100.0", message = "其他创新成果得分不能大于100")
    private Double otherInnovationAchievementScore; // 其他创新成果得分
}
