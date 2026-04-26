package com.example.service;

import ch.qos.logback.classic.Logger;
import com.example.pojo.ResponseMessage;
import com.example.pojo.StudentAcademicPerformance;
import com.example.pojo.dto.StudentAcademicPerformanceDto;
import com.example.repository.StudentAcademicPerformanceRepository;
import com.example.util.TableNameUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;


@Service
@Transactional
public class StudentAcademicPerformanceService implements IStudentAcademicPerformanceService {

    @Autowired
    private StudentAcademicPerformanceRepository studentAcademicPerformanceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<String, Object> getRadarData(String studentId, int year, int semester) {
        // 3.1 获取指定学生的学业信息
        StudentAcademicPerformance studentData = getByStudentId(studentId, year, semester);

        // 3.2 获取全部学生数据
        List<StudentAcademicPerformance> allStudentsData = getAll(year, semester);

        // 获取学生年级（学号前6位）
        String studentGrade = studentId.substring(0, 6);
        String studentType = studentData.getStudentType();

        // 筛选同年级同类型的学生数据
        List<StudentAcademicPerformance> filteredStudents = allStudentsData.stream()
                .filter(student -> student.getStudentId().startsWith(studentGrade) &&
                        student.getStudentType().equals(studentType))
                .collect(ArrayList::new, (list, item) -> list.add(item), (list1, list2) -> list1.addAll(list2));

        // 计算各项平均分
        double avgAcademicPerformance = filteredStudents.stream()
                .mapToDouble(StudentAcademicPerformance::getAcademicPerformanceScore)
                .average().orElse(0.0);

        double avgResearchProject = filteredStudents.stream()
                .mapToDouble(StudentAcademicPerformance::getResearchProjectScore)
                .average().orElse(0.0);

        double avgThesis = filteredStudents.stream()
                .mapToDouble(StudentAcademicPerformance::getThesisScore)
                .average().orElse(0.0);

        double avgAcademicPaper = filteredStudents.stream()
                .mapToDouble(StudentAcademicPerformance::getAcademicPaperScore)
                .average().orElse(0.0);

        double avgCompetition = filteredStudents.stream()
                .mapToDouble(StudentAcademicPerformance::getCompetitionScore)
                .average().orElse(0.0);

        double avgPatentSoftware = filteredStudents.stream()
                .mapToDouble(StudentAcademicPerformance::getPatentSoftwareScore)
                .average().orElse(0.0);

        // 构建scores map
        Map<String, Double> scores = new HashMap<>();
        scores.put("学业表现", studentData.getAcademicPerformanceScore());
        scores.put("科研项目", studentData.getResearchProjectScore());
        scores.put("学位论文", studentData.getThesisScore());
        scores.put("学术论文", studentData.getAcademicPaperScore());
        scores.put("竞赛获奖", studentData.getCompetitionScore());
        scores.put("专利软著", studentData.getPatentSoftwareScore());

        // 构建avgScores map
        Map<String, Double> avgScores = new HashMap<>();
        avgScores.put("平均学业表现", avgAcademicPerformance);
        avgScores.put("平均科研项目", avgResearchProject);
        avgScores.put("平均学位论文", avgThesis);
        avgScores.put("平均学术论文", avgAcademicPaper);
        avgScores.put("平均竞赛获奖", avgCompetition);
        avgScores.put("平均专利软著", avgPatentSoftware);

        // 构建最终结果
        Map<String, Object> result = new HashMap<>();
        result.put("studentId", studentData.getStudentId());
        result.put("studentName", studentData.getGraduateName());
        result.put("scores", scores);
        result.put("avgScores", avgScores);

        return result;
    }

    @Override
    public StudentAcademicPerformance add(StudentAcademicPerformanceDto dto, int year, int semester) {
        String tableName = TableNameUtils.generateTableName("student_academic_performance", year, semester);

        // 检查学号是否已存在
        String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE student_id = :studentId";
        Query checkQuery = entityManager.createNativeQuery(checkSql);
        checkQuery.setParameter("studentId", dto.getStudentId());
        Number count = (Number) checkQuery.getSingleResult();

        if (count.intValue() > 0) {
            throw new IllegalArgumentException("学号 " + dto.getStudentId() + " 已存在，无法重复添加");
        }

        // 构建插入SQL语句
        String insertSql = buildInsertSql(tableName);
        Query query = entityManager.createNativeQuery(insertSql);
        setQueryParameters(query, dto);
        query.executeUpdate();

        StudentAcademicPerformance entity = new StudentAcademicPerformance();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    @Override
    public List<StudentAcademicPerformance> create(List<StudentAcademicPerformanceDto> dtos, int year, int semester) {
        String tableName = TableNameUtils.generateTableName("student_academic_performance", year, semester);
        List<String> existingStudentIds = new ArrayList<>();

        // 检查是否有重复的学号
        for (StudentAcademicPerformanceDto dto : dtos) {
            String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE student_id = :studentId";
            Query checkQuery = entityManager.createNativeQuery(checkSql);
            checkQuery.setParameter("studentId", dto.getStudentId());
            Number count = (Number) checkQuery.getSingleResult();

            if (count.intValue() > 0) {
                existingStudentIds.add(dto.getStudentId());
            }
        }

        if (!existingStudentIds.isEmpty()) {
            throw new IllegalArgumentException("以下学号已存在，无法重复添加: " + String.join(", ", existingStudentIds));
        }

        // 批量插入数据
        for (StudentAcademicPerformanceDto dto : dtos) {
            String insertSql = buildInsertSql(tableName);
            Query query = entityManager.createNativeQuery(insertSql);
            setQueryParameters(query, dto);
            query.executeUpdate();
        }

        List<StudentAcademicPerformance> entities = new ArrayList<>();
        for (StudentAcademicPerformanceDto dto : dtos) {
            StudentAcademicPerformance entity = new StudentAcademicPerformance();
            BeanUtils.copyProperties(dto, entity);
            entities.add(entity);
        }
        return entities;
    }

    @Override
    public StudentAcademicPerformance getByStudentId(String studentId, int year, int semester) {
        String tableName = TableNameUtils.generateTableName("student_academic_performance", year, semester);
        String sql = "SELECT * FROM " + tableName + " WHERE student_id = :studentId";

        Query query = entityManager.createNativeQuery(sql, StudentAcademicPerformance.class);
        query.setParameter("studentId", studentId);

        List<StudentAcademicPerformance> results = query.getResultList();
        if (!results.isEmpty()) {
            return results.get(0);
        } else {
            throw new IllegalArgumentException("未找到学号为 " + studentId + " 的学业信息记录");
        }
    }

    @Override
    public List<StudentAcademicPerformance> getAll(int year, int semester) {
        try {
            String tableName = TableNameUtils.generateTableName("student_academic_performance", year, semester);
            String sql = "SELECT * FROM " + tableName;
            Query query = entityManager.createNativeQuery(sql, StudentAcademicPerformance.class);
            return query.getResultList();
        } catch (Exception e) {
            // 添加详细日志记录
            ResponseMessage logger = ResponseMessage.error(500, "查询数据失败", e);
            logger.error("查询学生学业成绩数据失败: 表名={}, 错误={}",
                    TableNameUtils.generateTableName("student_academic_performance", year, semester),
                    e.getMessage(), e);
            throw new RuntimeException("查询数据失败: " + e.getMessage(), e);
        }
    }


    @Override
    public void delete(String studentId, int year, int semester) {
        String tableName = TableNameUtils.generateTableName("student_academic_performance", year, semester);

        // 检查记录是否存在
        String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE student_id = :studentId";
        Query checkQuery = entityManager.createNativeQuery(checkSql);
        checkQuery.setParameter("studentId", studentId);
        Number count = (Number) checkQuery.getSingleResult();

        if (count.intValue() == 0) {
            throw new IllegalArgumentException("要删除的学业信息记录不存在，学号: " + studentId);
        }

        // 删除记录
        String deleteSql = "DELETE FROM " + tableName + " WHERE student_id = :studentId";
        Query query = entityManager.createNativeQuery(deleteSql);
        query.setParameter("studentId", studentId);
        query.executeUpdate();
    }

    @Override
    public StudentAcademicPerformance update(StudentAcademicPerformanceDto dto, int year, int semester) {
        String tableName = TableNameUtils.generateTableName("student_academic_performance", year, semester);

        // 检查记录是否存在
        String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE student_id = :studentId";
        Query checkQuery = entityManager.createNativeQuery(checkSql);
        checkQuery.setParameter("studentId", dto.getStudentId());
        Number count = (Number) checkQuery.getSingleResult();

        if (count.intValue() == 0) {
            throw new IllegalArgumentException("学号 " + dto.getStudentId() + " 不存在，无法更新");
        }

        // 更新记录
        String updateSql = buildUpdateSql(tableName);
        Query query = entityManager.createNativeQuery(updateSql);
        setUpdateQueryParameters(query, dto);
        query.executeUpdate();

        StudentAcademicPerformance entity = new StudentAcademicPerformance();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    @Override
    public List<StudentAcademicPerformance> batchUpdate(List<StudentAcademicPerformanceDto> dtos, int year, int semester) {
        String tableName = TableNameUtils.generateTableName("student_academic_performance", year, semester);
        List<StudentAcademicPerformance> entities = new ArrayList<>();

        for (StudentAcademicPerformanceDto dto : dtos) {
            // 检查学号是否存在
            String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE student_id = :studentId";
            Query checkQuery = entityManager.createNativeQuery(checkSql);
            checkQuery.setParameter("studentId", dto.getStudentId());
            Number count = (Number) checkQuery.getSingleResult();

            if (count.intValue() == 0) {
                throw new IllegalArgumentException("学号 " + dto.getStudentId() + " 不存在，无法更新");
            }

            // 更新记录
            String updateSql = buildUpdateSql(tableName);
            Query query = entityManager.createNativeQuery(updateSql);
            setUpdateQueryParameters(query, dto);
            query.executeUpdate();

            StudentAcademicPerformance entity = new StudentAcademicPerformance();
            BeanUtils.copyProperties(dto, entity);
            entities.add(entity);
        }

        return entities;
    }

    /**
     * 构建插入SQL语句
     */
    private String buildInsertSql(String tableName) {
        return "INSERT INTO " + tableName + " (" +
                "student_id, graduate_name, student_type, required_credits, earned_credits, " +
                "credit_completion_rate, total_required_credits, total_earned_credits, total_credit_completion_rate, " +
                "academic_performance_score, research_project_count, research_project_level, research_funding, research_project_ranking, project_role, " +
                "project_outcome_transformation, research_project_score, proposal_status, midterm_status, " +
                "review_status, review_score, defense_grade, outstanding_thesis, thesis_score, " +
                "academic_paper_count, publication_venue_level, author_role, highly_cited, impact_factor_above_10, " +
                "cover_paper, academic_paper_score, competition_award_count, competition_level, award_level, " +
                "competition_scope, competition_team_role, competition_score, patent_software_count, " +
                "patent_software_type, patent_software_scope, patent_software_team_role, patent_software_score, " +
                "academic_evaluation_score) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }



    /**
     * 设置查询参数
     */
    /**
     * 设置查询参数
     */
    private void setQueryParameters(Query query, StudentAcademicPerformanceDto dto) {
        query.setParameter(1, dto.getStudentId());
        query.setParameter(2, dto.getGraduateName());
        query.setParameter(3, dto.getStudentType());
        query.setParameter(4, dto.getRequiredCredits());
        query.setParameter(5, dto.getEarnedCredits());
        query.setParameter(6, dto.getCreditCompletionRate());
        query.setParameter(7, dto.getTotalRequiredCredits());
        query.setParameter(8, dto.getTotalEarnedCredits());
        query.setParameter(9, dto.getTotalCreditCompletionRate());
        query.setParameter(10, dto.getAcademicPerformanceScore());
        query.setParameter(11, dto.getResearchProjectCount());
        query.setParameter(12, dto.getResearchProjectLevel());
        query.setParameter(13, dto.getResearchFunding());
        query.setParameter(14, dto.getResearchProjectRanking());
        query.setParameter(15, dto.getProjectRole());
        query.setParameter(16, dto.getProjectOutcomeTransformation());
        query.setParameter(17, dto.getResearchProjectScore());
        query.setParameter(18, dto.getProposalStatus());
        query.setParameter(19, dto.getMidtermStatus());
        query.setParameter(20, dto.getReviewStatus());
        query.setParameter(21, dto.getReviewScore());
        query.setParameter(22, dto.getDefenseGrade());
        query.setParameter(23, dto.getOutstandingThesis());
        query.setParameter(24, dto.getThesisScore());
        query.setParameter(25, dto.getAcademicPaperCount());
        query.setParameter(26, dto.getPublicationVenueLevel());
        query.setParameter(27, dto.getAuthorRole());
        query.setParameter(28, dto.getHighlyCited());
        query.setParameter(29, dto.getImpactFactorAbove10());
        query.setParameter(30, dto.getCoverPaper());
        query.setParameter(31, dto.getAcademicPaperScore());
        query.setParameter(32, dto.getCompetitionAwardCount());
        query.setParameter(33, dto.getCompetitionLevel());
        query.setParameter(34, dto.getAwardLevel());
        query.setParameter(35, dto.getCompetitionScope());
        query.setParameter(36, dto.getCompetitionTeamRole());
        query.setParameter(37, dto.getCompetitionScore());
        query.setParameter(38, dto.getPatentSoftwareCount());
        query.setParameter(39, dto.getPatentSoftwareType());
        query.setParameter(40, dto.getPatentSoftwareScope());
        query.setParameter(41, dto.getPatentSoftwareTeamRole());
        query.setParameter(42, dto.getPatentSoftwareScore());
        query.setParameter(43, dto.getAcademicEvaluationScore());
    }


    /**
     * 构建更新SQL语句
     */
    private String buildUpdateSql(String tableName) {
        return "UPDATE " + tableName + " SET " +
                "graduate_name = ?, student_type = ?, required_credits = ?, earned_credits = ?, " +
                "credit_completion_rate = ?, total_required_credits = ?, total_earned_credits = ?, total_credit_completion_rate = ?, " +
                "academic_performance_score = ?, research_project_count = ?, research_project_level = ?, research_funding = ?, research_project_ranking = ?, project_role = ?, " +
                "project_outcome_transformation = ?, research_project_score = ?, proposal_status = ?, midterm_status = ?, " +
                "review_status = ?, review_score = ?, defense_grade = ?, outstanding_thesis = ?, thesis_score = ?, " +
                "academic_paper_count = ?, publication_venue_level = ?, author_role = ?, highly_cited = ?, impact_factor_above_10 = ?, " +
                "cover_paper = ?, academic_paper_score = ?, competition_award_count = ?, competition_level = ?, award_level = ?, " +
                "competition_scope = ?, competition_team_role = ?, competition_score = ?, patent_software_count = ?, " +
                "patent_software_type = ?, patent_software_scope = ?, patent_software_team_role = ?, patent_software_score = ?, " +
                "academic_evaluation_score = ? " +
                "WHERE student_id = ?";
    }


    /**
     * 设置更新查询参数
     */
    /**
     * 设置更新查询参数
     */
    private void setUpdateQueryParameters(Query query, StudentAcademicPerformanceDto dto) {
        query.setParameter(1, dto.getGraduateName());
        query.setParameter(2, dto.getStudentType());
        query.setParameter(3, dto.getRequiredCredits());
        query.setParameter(4, dto.getEarnedCredits());
        query.setParameter(5, dto.getCreditCompletionRate());
        query.setParameter(6, dto.getTotalRequiredCredits());
        query.setParameter(7, dto.getTotalEarnedCredits());
        query.setParameter(8, dto.getTotalCreditCompletionRate());
        query.setParameter(9, dto.getAcademicPerformanceScore());
        query.setParameter(10, dto.getResearchProjectCount());
        query.setParameter(11, dto.getResearchProjectLevel());
        query.setParameter(12, dto.getResearchFunding());
        query.setParameter(13, dto.getResearchProjectRanking());
        query.setParameter(14, dto.getProjectRole());
        query.setParameter(15, dto.getProjectOutcomeTransformation());
        query.setParameter(16, dto.getResearchProjectScore());
        query.setParameter(17, dto.getProposalStatus());
        query.setParameter(18, dto.getMidtermStatus());
        query.setParameter(19, dto.getReviewStatus());
        query.setParameter(20, dto.getReviewScore());
        query.setParameter(21, dto.getDefenseGrade());
        query.setParameter(22, dto.getOutstandingThesis());
        query.setParameter(23, dto.getThesisScore());
        query.setParameter(24, dto.getAcademicPaperCount());
        query.setParameter(25, dto.getPublicationVenueLevel());
        query.setParameter(26, dto.getAuthorRole());
        query.setParameter(27, dto.getHighlyCited());
        query.setParameter(28, dto.getImpactFactorAbove10());
        query.setParameter(29, dto.getCoverPaper());
        query.setParameter(30, dto.getAcademicPaperScore());
        query.setParameter(31, dto.getCompetitionAwardCount());
        query.setParameter(32, dto.getCompetitionLevel());
        query.setParameter(33, dto.getAwardLevel());
        query.setParameter(34, dto.getCompetitionScope());
        query.setParameter(35, dto.getCompetitionTeamRole());
        query.setParameter(36, dto.getCompetitionScore());
        query.setParameter(37, dto.getPatentSoftwareCount());
        query.setParameter(38, dto.getPatentSoftwareType());
        query.setParameter(39, dto.getPatentSoftwareScope());
        query.setParameter(40, dto.getPatentSoftwareTeamRole());
        query.setParameter(41, dto.getPatentSoftwareScore());
        query.setParameter(42, dto.getAcademicEvaluationScore());
        query.setParameter(43, dto.getStudentId());
    }

}
