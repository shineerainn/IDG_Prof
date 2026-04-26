package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "pg_academic")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PgAcademic {

    @Id
    @Column(name = "student_id")
    private String studentId; // 学生证号（主键）

    @Column(name = "graduate_name")
    private String graduateName; // 毕业生姓名

    @Column(name = "weighted_avg_grade")
    private Double weightedAvgGrade; // 加权平均成绩

    @Column(name = "core_course_grade")
    private Double coreCourseGrade; // 核心课程成绩

    @Column(name = "last_semester_avg_grade")
    private Double lastSemesterAvgGrade; // 上学期平均成绩

    @Column(name = "gpa_growth_rate")
    private Double gpaGrowthRate; // GPA增长率

    @Column(name = "grade_ranking")
    private Integer gradeRanking; // 成绩排名

    @Column(name = "ranking_level")
    private String rankingLevel; // 排名等级

    @Column(name = "academic_performance_score")
    private Double academicPerformanceScore; // 学业表现得分

    @Column(name = "research_project_level")
    private String researchProjectLevel; // 科研项目级别

    @Column(name = "project_role")
    private String projectRole; // 项目角色

    @Column(name = "project_achievement_transformation")
    private String projectAchievementTransformation; // 项目成果转化

    @Column(name = "research_project_score")
    private Double researchProjectScore; // 科研项目得分

    @Column(name = "thesis_defense_grade")
    private Integer thesisDefenseGrade; // 论文答辩成绩

    @Column(name = "thesis_innovativeness")
    private Integer thesisInnovativeness; // 论文创新性

    @Column(name = "thesis_practical_value")
    private Integer thesisPracticalValue; // 论文实用价值

    @Column(name = "thesis_achievement_transformation")
    private Integer thesisAchievementTransformation; // 论文成果转化

    @Column(name = "thesis_score")
    private Double thesisScore; // 论文得分

    @Column(name = "published_journal_conference")
    private String publishedJournalConference; // 发表期刊会议

    @Column(name = "author_level")
    private String authorLevel; // 作者级别

    @Column(name = "is_highly_cited")
    private String isHighlyCited; // 是否高被引

    @Column(name = "is_if_above")
    private String isIfAbove; // 是否影响因子以上

    @Column(name = "is_cover_paper")
    private String isCoverPaper; // 是否封面论文

    @Column(name = "academic_paper_score")
    private Double academicPaperScore; // 学术论文得分

    @Column(name = "competition_level")
    private String competitionLevel; // 竞赛级别

    @Column(name = "award_level")
    private String awardLevel; // 获奖级别

    @Column(name = "competition_scope")
    private String competitionScope; // 竞赛范围

    @Column(name = "competition_team_role")
    private String competitionTeamRole; // 竞赛团队角色

    @Column(name = "competition_score")
    private Double competitionScore; // 竞赛得分

    @Column(name = "patent_software_type")
    private String patentSoftwareType; // 专利软件类型

    @Column(name = "legal_status_coefficient")
    private String legalStatusCoefficient; // 法律状态系数

    @Column(name = "technology_transfer_amount")
    private String technologyTransferAmount; // 技术转让金额

    @Column(name = "patent_software_score")
    private Double patentSoftwareScore; // 专利软件得分

    @Column(name = "has_startup_project")
    private String hasStartupProject; // 是否有创业项目

    @Column(name = "startup_funding_stage")
    private String startupFundingStage; // 创业融资阶段

    @Column(name = "is_company_registered")
    private String isCompanyRegistered; // 是否注册公司

    @Column(name = "has_startup_competition_award")
    private String hasStartupCompetitionAward; // 是否有创业竞赛获奖

    @Column(name = "tech_application_enterprise_count")
    private Integer techApplicationEnterpriseCount; // 技术应用企业数量

    @Column(name = "tech_promotion_contract_amount")
    private String techPromotionContractAmount; // 技术推广合同金额

    @Column(name = "other_innovation_achievement_score")
    private Double otherInnovationAchievementScore; // 其他创新成果得分
}
