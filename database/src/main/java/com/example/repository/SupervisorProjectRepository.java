package com.example.repository;

import com.example.pojo.SupervisorProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 导师项目仓库接口
 */
@Repository
public interface SupervisorProjectRepository extends JpaRepository<SupervisorProject, String> {
    
    /**
     * 根据工号查找导师项目
     * @param workId 工号
     * @return 导师项目信息
     */
    SupervisorProject findByWorkId(String workId);
    
    /**
     * 根据导师姓名查找项目列表
     * @param supervisorName 导师姓名
     * @return 该项目列表
     */
    List<SupervisorProject> findBySupervisorName(String supervisorName);
    
    /**
     * 根据单位查找项目列表
     * @param organization 单位
     * @return 该单位的项目列表
     */
    List<SupervisorProject> findByOrganization(String organization);
    
    /**
     * 根据项目名称查找项目列表
     * @param projectName 项目名称
     * @return 该项目列表
     */
    List<SupervisorProject> findByProjectName(String projectName);
    
    /**
     * 根据项目编号查找项目
     * @param projectNumber 项目编号
     * @return 导师项目信息
     */
    SupervisorProject findByProjectNumber(String projectNumber);
    
    /**
     * 根据项目级别查找项目列表
     * @param projectLevel 项目级别
     * @return 该项目列表
     */
    List<SupervisorProject> findByProjectLevel(String projectLevel);
    
    /**
     * 根据项目来源查找项目列表
     * @param fundingSource 项目来源
     * @return 该项目列表
     */
    List<SupervisorProject> findByFundingSource(String fundingSource);
    
    /**
     * 根据项目类型查找项目列表
     * @param projectType 项目类型
     * @return 该项目列表
     */
    List<SupervisorProject> findByProjectType(String projectType);
    
    /**
     * 根据立项日期范围查找项目列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 在指定日期范围内的项目列表
     */
    List<SupervisorProject> findByApprovalDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * 根据结项日期范围查找项目列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 在指定日期范围内的项目列表
     */
    List<SupervisorProject> findByCompletionDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 根据项目性质查找项目列表
     * @param projectNature 项目性质
     * @return 该项目列表
     */
    List<SupervisorProject> findByProjectNature(String projectNature);

}
