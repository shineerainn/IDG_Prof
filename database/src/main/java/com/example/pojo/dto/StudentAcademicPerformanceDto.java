package com.example.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAcademicPerformanceDto {

    @NotBlank(message = "学生证号不能为空")
    private String studentId; // 学生证号

    @NotBlank(message = "姓名不能为空")
    private String graduateName; // 姓名

    @NotBlank(message = "学生类型不能为空")
    private String studentType; // 学生类型

    @NotNull(message = "应修学分不能为空")
    private Integer requiredCredits; // 应修学分（不含学位论文）

    @NotNull(message = "已修学分不能为空")
    private Integer earnedCredits; // 已修学分（不含学位论文）

    @NotNull(message = "应修学分进度不能为空")
    private Double creditCompletionRate; // 应修学分进度（不含学位论文）

    @NotNull(message = "应修总学分不能为空")
    private Integer totalRequiredCredits; // 应修总学分

    @NotNull(message = "已修总学分不能为空")
    private Integer totalEarnedCredits; // 已修总学分

    @NotNull(message = "应修总学分进度不能为空")
    private Double totalCreditCompletionRate; // 应修总学分进度

    @NotNull(message = "学业表现得分不能为空")
    private Double academicPerformanceScore; // 学业表现得分

    @NotNull(message = "科研项目数量不能为空")
    private Integer researchProjectCount; // 科研项目数量

    @NotBlank(message = "科研项目级别不能为空")
    private String researchProjectLevel; // 科研项目级别

    @NotNull(message = "科研项目经费不能为空")
    private Double researchFunding; // 科研项目经费

    @NotNull(message = "科研项目位次不能为空")
    private Double researchProjectRanking; // 科研项目位次


    @NotBlank(message = "项目担任角色不能为空")
    private String projectRole; // 项目担任角色

    @NotBlank(message = "项目成果转化不能为空")
    private String projectOutcomeTransformation; // 项目成果转化

    @NotNull(message = "科研项目得分不能为空")
    private Double researchProjectScore; // 科研项目得分

    @NotBlank(message = "开题状态不能为空")
    private String proposalStatus; // 开题状态

    @NotBlank(message = "中期状态不能为空")
    private String midtermStatus; // 中期状态

    @NotBlank(message = "评审状态不能为空")
    private String reviewStatus; // 评审状态

    @NotBlank(message = "评审成绩不能为空")
    private String reviewScore; // 评审成绩

    @NotBlank(message = "答辩成绩不能为空")
    private String defenseGrade; // 答辩成绩

    @NotBlank(message = "是否优秀论文不能为空")
    private String outstandingThesis; // 是否优秀论文

    @NotNull(message = "学位论文得分不能为空")
    private Double thesisScore; // 学位论文得分

    @NotNull(message = "学术论文数量不能为空")
    private Integer academicPaperCount; // 学术论文数量

    @NotBlank(message = "发表期刊会议等级不能为空")
    private String publicationVenueLevel; // 发表期刊会议等级

    @NotBlank(message = "作者等级不能为空")
    private String authorRole; // 作者等级

    @NotBlank(message = "是否高被引不能为空")
    private String highlyCited; // 是否高被引

    @NotBlank(message = "期刊影响因子是否大于10不能为空")
    private String impactFactorAbove10; // 期刊影响因子是否大于10

    @NotBlank(message = "是否为封面论文不能为空")
    private String coverPaper; // 是否为封面论文

    @NotNull(message = "学术论文得分不能为空")
    private Double academicPaperScore; // 学术论文得分

    @NotNull(message = "竞赛获奖数量不能为空")
    private Integer competitionAwardCount; // 竞赛获奖数量

    @NotBlank(message = "竞赛级别不能为空")
    private String competitionLevel; // 竞赛级别

    @NotBlank(message = "获奖级别不能为空")
    private String awardLevel; // 获奖级别

    @NotBlank(message = "竞赛范围不能为空")
    private String competitionScope; // 竞赛范围

    @NotBlank(message = "竞赛团队角色不能为空")
    private String competitionTeamRole; // 竞赛团队角色

    @NotNull(message = "竞赛获奖得分不能为空")
    private Double competitionScore; // 竞赛获奖得分

    @NotNull(message = "专利软著数量不能为空")
    private Integer patentSoftwareCount; // 专利软著数量

    @NotBlank(message = "专利软著类型不能为空")
    private String patentSoftwareType; // 专利软著类型

    @NotBlank(message = "专利范围不能为空")
    private String patentSoftwareScope; // 专利范围

    @NotBlank(message = "专利团队角色不能为空")
    private String patentSoftwareTeamRole; // 专利团队角色

    @NotNull(message = "专利软著得分不能为空")
    private Double patentSoftwareScore; // 专利软著得分

    @NotNull(message = "学业评价得分不能为空")
    private Double academicEvaluationScore; // 学业评价得分
}
