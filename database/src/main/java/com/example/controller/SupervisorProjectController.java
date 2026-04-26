package com.example.controller;

import com.example.pojo.SupervisorProject;
import com.example.pojo.ResponseMessage;
import com.example.pojo.dto.SupervisorProjectDto;
import com.example.service.ISupervisorProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/database/supervisorProject")
@Slf4j
public class SupervisorProjectController {

    @Autowired
    private ISupervisorProjectService supervisorProjectService;

    /**
     * 新增导师项目数据
     * @param dto 导师项目数据传输对象
     * @return 新增的记录
     */
    @PostMapping
    public ResponseMessage<SupervisorProject> add(@Validated @RequestBody SupervisorProjectDto dto) {
        SupervisorProject entity = supervisorProjectService.saveSupervisorProject(dto);
        return ResponseMessage.success(entity);
    }

    /**
     * 批量新增导师项目数据
     * @param dtos 导师项目数据传输对象列表
     * @return 新增的记录列表
     */
    @PostMapping("/all")
    public ResponseMessage<List<SupervisorProject>> create(@Validated @RequestBody List<SupervisorProjectDto> dtos) {
        // 注意：这里需要在 service 中添加批量保存方法
        // 由于原 service 接口未提供此方法，建议添加 batchSaveSupervisorProjects 方法
        return ResponseMessage.success();
    }

    /**
     * 根据工号查询导师项目信息
     * @param workId 工号
     * @return 导师项目记录
     */
    @GetMapping("/{workId}")
    public ResponseMessage<SupervisorProject> getByWorkId(@PathVariable String workId) {
        SupervisorProject entity = supervisorProjectService.getSupervisorProjectById(workId);
        return ResponseMessage.success(entity);
    }

    /**
     * 查询全部导师项目信息
     * @return 导师项目记录列表
     */
    @GetMapping
    public ResponseMessage<List<SupervisorProject>> getAll() {
        List<SupervisorProject> entities = supervisorProjectService.getAllSupervisorProjects();
        return ResponseMessage.success(entities);
    }

    /**
     * 根据工号更新导师项目信息
     * @param dto 导师项目数据传输对象
     * @return 更新后的记录
     */
    @PutMapping
    public ResponseMessage<SupervisorProject> update(@Validated @RequestBody SupervisorProjectDto dto) {
        SupervisorProject entity = supervisorProjectService.updateSupervisorProject(dto.getWorkId(), dto);
        return ResponseMessage.success(entity);
    }

    /**
     * 批量更新导师项目信息
     * @param dtos 导师项目数据传输对象列表
     * @return 更新后的记录列表
     */
    @PutMapping("/batch")
    public ResponseMessage<List<SupervisorProject>> batchUpdate(@Validated @RequestBody List<SupervisorProjectDto> dtos) {
        // 注意：这里需要在 service 中添加批量更新方法
        // 建议添加 batchUpdateSupervisorProjects 方法
        return ResponseMessage.success();
    }

    /**
     * 根据工号删除导师项目信息
     * @param workId 工号
     * @return 操作结果
     */
    @DeleteMapping("/{workId}")
    public ResponseMessage<Void> delete(@PathVariable String workId) {
        supervisorProjectService.deleteSupervisorProject(workId);
        return ResponseMessage.success();
    }

    /**
     * 根据导师姓名查询项目列表
     * @param supervisorName 导师姓名
     * @return 导师项目记录列表
     */
    @GetMapping("/supervisorName/{supervisorName}")
    public ResponseMessage<List<SupervisorProject>> getBySupervisorName(@PathVariable String supervisorName) {
        List<SupervisorProject> entities = supervisorProjectService.getSupervisorProjectsBySupervisorName(supervisorName);
        return ResponseMessage.success(entities);
    }

    /**
     * 根据单位查询项目列表
     * @param organization 单位
     * @return 导师项目记录列表
     */
    @GetMapping("/organization/{organization}")
    public ResponseMessage<List<SupervisorProject>> getByOrganization(@PathVariable String organization) {
        List<SupervisorProject> entities = supervisorProjectService.getSupervisorProjectsByOrganization(organization);
        return ResponseMessage.success(entities);
    }

    /**
     * 根据项目名称查询项目列表
     * @param projectName 项目名称
     * @return 导师项目记录列表
     */
    @GetMapping("/projectName/{projectName}")
    public ResponseMessage<List<SupervisorProject>> getByProjectName(@PathVariable String projectName) {
        List<SupervisorProject> entities = supervisorProjectService.getSupervisorProjectsByProjectName(projectName);
        return ResponseMessage.success(entities);
    }

    /**
     * 根据项目编号查询项目
     * @param projectNumber 项目编号
     * @return 导师项目记录
     */
    @GetMapping("/projectNumber/{projectNumber}")
    public ResponseMessage<SupervisorProject> getByProjectNumber(@PathVariable String projectNumber) {
        SupervisorProject entity = supervisorProjectService.getSupervisorProjectByProjectNumber(projectNumber);
        return ResponseMessage.success(entity);
    }

    /**
     * 根据项目级别查询项目列表
     * @param projectLevel 项目级别
     * @return 导师项目记录列表
     */
    @GetMapping("/projectLevel/{projectLevel}")
    public ResponseMessage<List<SupervisorProject>> getByProjectLevel(@PathVariable String projectLevel) {
        List<SupervisorProject> entities = supervisorProjectService.getSupervisorProjectsByProjectLevel(projectLevel);
        return ResponseMessage.success(entities);
    }

    /**
     * 根据项目来源查询项目列表
     * @param fundingSource 项目来源
     * @return 导师项目记录列表
     */
    @GetMapping("/fundingSource/{fundingSource}")
    public ResponseMessage<List<SupervisorProject>> getByFundingSource(@PathVariable String fundingSource) {
        List<SupervisorProject> entities = supervisorProjectService.getSupervisorProjectsByFundingSource(fundingSource);
        return ResponseMessage.success(entities);
    }

    /**
     * 根据项目类型查询项目列表
     * @param projectType 项目类型
     * @return 导师项目记录列表
     */
    @GetMapping("/projectType/{projectType}")
    public ResponseMessage<List<SupervisorProject>> getByProjectType(@PathVariable String projectType) {
        List<SupervisorProject> entities = supervisorProjectService.getSupervisorProjectsByProjectType(projectType);
        return ResponseMessage.success(entities);
    }

    /**
     * 根据项目性质查询项目列表
     * @param projectNature 项目性质
     * @return 导师项目记录列表
     */
    @GetMapping("/projectNature/{projectNature}")
    public ResponseMessage<List<SupervisorProject>> getByProjectNature(@PathVariable String projectNature) {
        List<SupervisorProject> entities = supervisorProjectService.getSupervisorProjectsByProjectNature(projectNature);
        return ResponseMessage.success(entities);
    }

    /**
     * 根据立项日期范围查询项目列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 导师项目记录列表
     */
    @GetMapping("/approvalDate")
    public ResponseMessage<List<SupervisorProject>> getByApprovalDateBetween(
            @RequestParam LocalDate startDate, 
            @RequestParam LocalDate endDate) {
        List<SupervisorProject> entities = supervisorProjectService.getSupervisorProjectsByApprovalDateBetween(startDate, endDate);
        return ResponseMessage.success(entities);
    }

    /**
     * 根据结项日期范围查询项目列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 导师项目记录列表
     */
    @GetMapping("/completionDate")
    public ResponseMessage<List<SupervisorProject>> getByCompletionDateBetween(
            @RequestParam LocalDate startDate, 
            @RequestParam LocalDate endDate) {
        List<SupervisorProject> entities = supervisorProjectService.getSupervisorProjectsByCompletionDateBetween(startDate, endDate);
        return ResponseMessage.success(entities);
    }
}
