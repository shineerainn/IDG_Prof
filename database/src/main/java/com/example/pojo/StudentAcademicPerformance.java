package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAcademicPerformance {

    @Id
    @Column(name = "student_id")
    private String studentId; // 学生证号

    @Column(name = "graduate_name")
    private String graduateName; // 姓名

    @Column(name = "student_type")
    private String studentType; // 学生类型

    @Column(name = "required_credits")
    private Integer requiredCredits; // 应修学分（不含学位论文）

    @Column(name = "earned_credits")
    private Integer earnedCredits; // 已修学分（不含学位论文）

    @Column(name = "credit_completion_rate")
    private Double creditCompletionRate; // 应修学分进度（不含学位论文）

    @Column(name = "total_required_credits")
    private Integer totalRequiredCredits; // 应修总学分

    @Column(name = "total_earned_credits")
    private Integer totalEarnedCredits; // 已修总学分

    @Column(name = "total_credit_completion_rate")
    private Double totalCreditCompletionRate; // 应修总学分进度

    @Column(name = "academic_performance_score")
    private Double academicPerformanceScore; // 学业表现得分

    @Column(name = "research_project_count")
    private Integer researchProjectCount; // 科研项目数量

    @Column(name = "research_project_level")
    private String researchProjectLevel; // 科研项目级别

    @Column(name = "research_funding")
    private Double researchFunding; // 科研项目经费

    @Column(name = "research_project_ranking")
    private Double researchProjectRanking; // 科研项目位次

    @Column(name = "project_role")
    private String projectRole; // 项目担任角色

    @Column(name = "project_outcome_transformation")
    private String projectOutcomeTransformation; // 项目成果转化

    @Column(name = "research_project_score")
    private Double researchProjectScore; // 科研项目得分

    @Column(name = "proposal_status")
    private String proposalStatus; // 开题状态

    @Column(name = "midterm_status")
    private String midtermStatus; // 中期状态

    @Column(name = "review_status")
    private String reviewStatus; // 评审状态

    @Column(name = "review_score")
    private String reviewScore; // 评审成绩

    @Column(name = "defense_grade")
    private String defenseGrade; // 答辩成绩

    @Column(name = "outstanding_thesis")
    private String outstandingThesis; // 是否优秀论文

    @Column(name = "thesis_score")
    private Double thesisScore; // 学位论文得分

    @Column(name = "academic_paper_count")
    private Integer academicPaperCount; // 学术论文数量

    @Column(name = "publication_venue_level")
    private String publicationVenueLevel; // 发表期刊会议等级

    @Column(name = "author_role")
    private String authorRole; // 作者等级

    @Column(name = "highly_cited")
    private String highlyCited; // 是否高被引

    @Column(name = "impact_factor_above_10")
    private String impactFactorAbove10; // 期刊影响因子是否大于10

    @Column(name = "cover_paper")
    private String coverPaper; // 是否为封面论文

    @Column(name = "academic_paper_score")
    private Double academicPaperScore; // 学术论文得分

    @Column(name = "competition_award_count")
    private Integer competitionAwardCount; // 竞赛获奖数量

    @Column(name = "competition_level")
    private String competitionLevel; // 竞赛级别

    @Column(name = "award_level")
    private String awardLevel; // 获奖级别

    @Column(name = "competition_scope")
    private String competitionScope; // 竞赛范围

    @Column(name = "competition_team_role")
    private String competitionTeamRole; // 竞赛团队角色

    @Column(name = "competition_score")
    private Double competitionScore; // 竞赛获奖得分

    @Column(name = "patent_software_count")
    private Integer patentSoftwareCount; // 专利软著数量

    @Column(name = "patent_software_type")
    private String patentSoftwareType; // 专利软著类型

    @Column(name = "patent_software_scope")
    private String patentSoftwareScope; // 专利范围

    @Column(name = "patent_software_team_role")
    private String patentSoftwareTeamRole; // 专利团队角色

    @Column(name = "patent_software_score")
    private Double patentSoftwareScore; // 专利软著得分

    @Column(name = "academic_evaluation_score")
    private Double academicEvaluationScore; // 学业评价得分
}