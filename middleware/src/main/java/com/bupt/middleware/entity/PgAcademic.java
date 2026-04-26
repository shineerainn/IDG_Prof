package com.bupt.middleware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PgAcademic {

    private String studentId; // 1、学生证号（主键）

    private String graduateName; // 2、毕业生姓名

    private String weightedAvgGrade; // 3、加权平均成绩

    private String coreCourseGrade; // 4、核心课程成绩

    private String lastSemesterAvgGrade; // 5、上学期平均成绩

    private String gpaGrowthRate; // 6、GPA增长率

    private String gradeRanking; // 7、成绩排名

    private String rankingLevel; // 8、排名等级

    private String academicPerformanceScore; // 9、学业表现得分

    private String researchProjectLevel; // 10、科研项目级别

    private String projectRole; // 11、项目角色

    private String projectAchievementTransformation; // 12、项目成果转化

    private String researchProjectScore; // 13、科研项目得分

    private String thesisDefenseGrade; // 14、论文答辩成绩

    private String thesisInnovativeness; // 15、论文创新性

    private String thesisPracticalValue; // 16、论文实用价值

    private String thesisAchievementTransformation; // 17、论文成果转化

    private String thesisScore; // 18、论文得分

    private String publishedJournalConference; // 19、发表期刊会议

    private String authorLevel; // 20、作者级别

    private String isHighlyCited; // 21、是否高被引

    private String isIfAbove; // 22、是否影响因子以上

    private String isCoverPaper; // 23、是否封面论文

    private String academicPaperScore; // 24、学术论文得分

    private String competitionLevel; // 25、竞赛级别

    private String awardLevel; // 26、获奖级别

    private String competitionScope; // 27、竞赛范围

    private String competitionTeamRole; // 28、竞赛团队角色

    private String competitionScore; // 29、竞赛得分

    private String patentSoftwareType; // 30、专利软件类型

    private String legalStatusCoefficient; // 31、法律状态系数

    private String technologyTransferAmount; // 32、技术转让金额

    private String patentSoftwareScore; // 33、专利软件得分

    private String hasStartupProject; // 34、是否有创业项目

    private String startupFundingStage; // 35、创业融资阶段

    private String isCompanyRegistered; // 36、是否注册公司

    private String hasStartupCompetitionAward; // 37、是否有创业竞赛获奖

    private String techApplicationEnterpriseCount; // 38、技术应用企业数量

    private String techPromotionContractAmount; // 39、技术推广合同金额

    private String otherInnovationAchievementScore; // 40、其他创新成果得分
}
