// ISupervisorProjectService.java
package com.example.service;

import com.example.pojo.SupervisorProject;
import com.example.pojo.dto.SupervisorProjectDto;

import java.time.LocalDate;
import java.util.List;

/**
 * 导师项目服务接口
 */
public interface ISupervisorProjectService {

    /**
     * 获取所有导师项目
     * @return 导师项目列表
     */
    List<SupervisorProject> getAllSupervisorProjects();

    /**
     * 根据工号获取导师项目
     * @param workId 工号
     * @return 导师项目信息
     */
    SupervisorProject getSupervisorProjectById(String workId);

    /**
     * 根据导师姓名获取项目列表
     * @param supervisorName 导师姓名
     * @return 该项目列表
     */
    List<SupervisorProject> getSupervisorProjectsBySupervisorName(String supervisorName);

    /**
     * 根据单位获取项目列表
     * @param organization 单位
     * @return 该单位的项目列表
     */
    List<SupervisorProject> getSupervisorProjectsByOrganization(String organization);

    /**
     * 根据项目名称获取项目列表
     * @param projectName 项目名称
     * @return 该项目列表
     */
    List<SupervisorProject> getSupervisorProjectsByProjectName(String projectName);

    /**
     * 根据项目编号获取项目
     * @param projectNumber 项目编号
     * @return 导师项目信息
     */
    SupervisorProject getSupervisorProjectByProjectNumber(String projectNumber);

    /**
     * 根据项目级别获取项目列表
     * @param projectLevel 项目级别
     * @return 该项目列表
     */
    List<SupervisorProject> getSupervisorProjectsByProjectLevel(String projectLevel);

    /**
     * 根据项目来源获取项目列表
     * @param fundingSource 项目来源
     * @return 该项目列表
     */
    List<SupervisorProject> getSupervisorProjectsByFundingSource(String fundingSource);

    /**
     * 根据项目类型获取项目列表
     * @param projectType 项目类型
     * @return 该项目列表
     */
    List<SupervisorProject> getSupervisorProjectsByProjectType(String projectType);

    /**
     * 根据项目性质获取项目列表
     * @param projectNature 项目性质
     * @return 该项目列表
     */
    List<SupervisorProject> getSupervisorProjectsByProjectNature(String projectNature);

    /**
     * 根据立项日期范围获取项目列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 在指定日期范围内的项目列表
     */
    List<SupervisorProject> getSupervisorProjectsByApprovalDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 根据结项日期范围获取项目列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 在指定日期范围内的项目列表
     */
    List<SupervisorProject> getSupervisorProjectsByCompletionDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 保存导师项目
     * @param supervisorProjectDto 导师项目DTO
     * @return 保存后的导师项目
     */
    SupervisorProject saveSupervisorProject(SupervisorProjectDto supervisorProjectDto);

    /**
     * 更新导师项目
     * @param workId 工号
     * @param supervisorProjectDto 导师项目DTO
     * @return 更新后的导师项目
     */
    SupervisorProject updateSupervisorProject(String workId, SupervisorProjectDto supervisorProjectDto);

    /**
     * 删除导师项目
     * @param workId 工号
     */
    void deleteSupervisorProject(String workId);
}
