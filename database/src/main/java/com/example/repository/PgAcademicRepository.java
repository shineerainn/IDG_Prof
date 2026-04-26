package com.example.repository;

import com.example.pojo.PgAcademic;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PgAcademicRepository extends CrudRepository<PgAcademic, String> {

    /**
     * 根据学号查找学术信息
     */
    Optional<PgAcademic> findByStudentId(String studentId);

    /**
     * 根据学号删除学术信息
     */
    @Modifying
    @Query("DELETE FROM PgAcademic p WHERE p.studentId = :studentId")
    void deleteByStudentId(@Param("studentId") String studentId);

    /**
     * 根据学号更新学术信息
     */
    @Modifying
    @Query("UPDATE PgAcademic p SET " +
            "p.graduateName = :graduateName, " +
            "p.weightedAvgGrade = :weightedAvgGrade, " +
            "p.coreCourseGrade = :coreCourseGrade, " +
            "p.lastSemesterAvgGrade = :lastSemesterAvgGrade, " +
            "p.gpaGrowthRate = :gpaGrowthRate, " +
            "p.gradeRanking = :gradeRanking, " +
            "p.rankingLevel = :rankingLevel, " +
            "p.academicPerformanceScore = :academicPerformanceScore, " +
            "p.researchProjectLevel = :researchProjectLevel, " +
            "p.projectRole = :projectRole, " +
            "p.projectAchievementTransformation = :projectAchievementTransformation, " +
            "p.researchProjectScore = :researchProjectScore, " +
            "p.thesisDefenseGrade = :thesisDefenseGrade, " +
            "p.thesisInnovativeness = :thesisInnovativeness, " +
            "p.thesisPracticalValue = :thesisPracticalValue, " +
            "p.thesisAchievementTransformation = :thesisAchievementTransformation, " +
            "p.thesisScore = :thesisScore, " +
            "p.publishedJournalConference = :publishedJournalConference, " +
            "p.authorLevel = :authorLevel, " +
            "p.isHighlyCited = :isHighlyCited, " +
            "p.isIfAbove = :isIfAbove, " +
            "p.isCoverPaper = :isCoverPaper, " +
            "p.academicPaperScore = :academicPaperScore, " +
            "p.competitionLevel = :competitionLevel, " +
            "p.awardLevel = :awardLevel, " +
            "p.competitionScope = :competitionScope, " +
            "p.competitionTeamRole = :competitionTeamRole, " +
            "p.competitionScore = :competitionScore, " +
            "p.patentSoftwareType = :patentSoftwareType, " +
            "p.legalStatusCoefficient = :legalStatusCoefficient, " +
            "p.technologyTransferAmount = :technologyTransferAmount, " +
            "p.patentSoftwareScore = :patentSoftwareScore, " +
            "p.hasStartupProject = :hasStartupProject, " +
            "p.startupFundingStage = :startupFundingStage, " +
            "p.isCompanyRegistered = :isCompanyRegistered, " +
            "p.hasStartupCompetitionAward = :hasStartupCompetitionAward, " +
            "p.techApplicationEnterpriseCount = :techApplicationEnterpriseCount, " +
            "p.techPromotionContractAmount = :techPromotionContractAmount, " +
            "p.otherInnovationAchievementScore = :otherInnovationAchievementScore " +
            "WHERE p.studentId = :studentId")
    void updateByStudentId(
            @Param("studentId") String studentId,
            @Param("graduateName") String graduateName,
            @Param("weightedAvgGrade") Double weightedAvgGrade,
            @Param("coreCourseGrade") Double coreCourseGrade,
            @Param("lastSemesterAvgGrade") Double lastSemesterAvgGrade,
            @Param("gpaGrowthRate") Double gpaGrowthRate,
            @Param("gradeRanking") Integer gradeRanking,
            @Param("rankingLevel") String rankingLevel,
            @Param("academicPerformanceScore") Double academicPerformanceScore,
            @Param("researchProjectLevel") String researchProjectLevel,
            @Param("projectRole") String projectRole,
            @Param("projectAchievementTransformation") String projectAchievementTransformation,
            @Param("researchProjectScore") Double researchProjectScore,
            @Param("thesisDefenseGrade") Integer thesisDefenseGrade,
            @Param("thesisInnovativeness") Integer thesisInnovativeness,
            @Param("thesisPracticalValue") Integer thesisPracticalValue,
            @Param("thesisAchievementTransformation") Integer thesisAchievementTransformation,
            @Param("thesisScore") Double thesisScore,
            @Param("publishedJournalConference") String publishedJournalConference,
            @Param("authorLevel") String authorLevel,
            @Param("isHighlyCited") String isHighlyCited,
            @Param("isIfAbove") String isIfAbove,
            @Param("isCoverPaper") String isCoverPaper,
            @Param("academicPaperScore") Double academicPaperScore,
            @Param("competitionLevel") String competitionLevel,
            @Param("awardLevel") String awardLevel,
            @Param("competitionScope") String competitionScope,
            @Param("competitionTeamRole") String competitionTeamRole,
            @Param("competitionScore") Double competitionScore,
            @Param("patentSoftwareType") String patentSoftwareType,
            @Param("legalStatusCoefficient") String legalStatusCoefficient,
            @Param("technologyTransferAmount") String technologyTransferAmount,
            @Param("patentSoftwareScore") Double patentSoftwareScore,
            @Param("hasStartupProject") String hasStartupProject,
            @Param("startupFundingStage") String startupFundingStage,
            @Param("isCompanyRegistered") String isCompanyRegistered,
            @Param("hasStartupCompetitionAward") String hasStartupCompetitionAward,
            @Param("techApplicationEnterpriseCount") Integer techApplicationEnterpriseCount,
            @Param("techPromotionContractAmount") String techPromotionContractAmount,
            @Param("otherInnovationAchievementScore") Double otherInnovationAchievementScore
    );

    /**
     * 检查学号是否存在
     */
    boolean existsByStudentId(String studentId);

}
