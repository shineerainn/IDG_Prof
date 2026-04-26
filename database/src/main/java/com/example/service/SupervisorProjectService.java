package com.example.service;

import com.example.pojo.SupervisorProject;
import com.example.pojo.dto.SupervisorProjectDto;
import com.example.repository.SupervisorProjectRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 导师项目服务实现类
 */
@Service
public class SupervisorProjectService implements ISupervisorProjectService {
    
    private final SupervisorProjectRepository supervisorProjectRepository;
    
    @Autowired
    public SupervisorProjectService(SupervisorProjectRepository supervisorProjectRepository) {
        this.supervisorProjectRepository = supervisorProjectRepository;
    }
    
    @Override
    public List<SupervisorProject> getAllSupervisorProjects() {
        return supervisorProjectRepository.findAll();
    }
    
    @Override
    public SupervisorProject getSupervisorProjectById(String workId) {
        return supervisorProjectRepository.findById(workId).orElse(null);
    }
    
    @Override
    public List<SupervisorProject> getSupervisorProjectsBySupervisorName(String supervisorName) {
        return supervisorProjectRepository.findBySupervisorName(supervisorName);
    }
    
    @Override
    public List<SupervisorProject> getSupervisorProjectsByOrganization(String organization) {
        return supervisorProjectRepository.findByOrganization(organization);
    }
    
    @Override
    public List<SupervisorProject> getSupervisorProjectsByProjectName(String projectName) {
        return supervisorProjectRepository.findByProjectName(projectName);
    }
    
    @Override
    public SupervisorProject getSupervisorProjectByProjectNumber(String projectNumber) {
        return supervisorProjectRepository.findByProjectNumber(projectNumber);
    }
    
    @Override
    public List<SupervisorProject> getSupervisorProjectsByProjectLevel(String projectLevel) {
        return supervisorProjectRepository.findByProjectLevel(projectLevel);
    }
    
    @Override
    public List<SupervisorProject> getSupervisorProjectsByFundingSource(String fundingSource) {
        return supervisorProjectRepository.findByFundingSource(fundingSource);
    }
    
    @Override
    public List<SupervisorProject> getSupervisorProjectsByProjectType(String projectType) {
        return supervisorProjectRepository.findByProjectType(projectType);
    }
    
    @Override
    public List<SupervisorProject> getSupervisorProjectsByProjectNature(String projectNature) {
        return supervisorProjectRepository.findByProjectNature(projectNature);
    }
    
    @Override
    public List<SupervisorProject> getSupervisorProjectsByApprovalDateBetween(LocalDate startDate, LocalDate endDate) {
        return supervisorProjectRepository.findByApprovalDateBetween(startDate, endDate);
    }
    
    @Override
    public List<SupervisorProject> getSupervisorProjectsByCompletionDateBetween(LocalDate startDate, LocalDate endDate) {
        return supervisorProjectRepository.findByCompletionDateBetween(startDate, endDate);
    }
    
    @Override
    public SupervisorProject saveSupervisorProject(SupervisorProjectDto supervisorProjectDto) {
        SupervisorProject supervisorProject = new SupervisorProject();
        BeanUtils.copyProperties(supervisorProjectDto, supervisorProject);
        return supervisorProjectRepository.save(supervisorProject);
    }
    
    @Override
    public SupervisorProject updateSupervisorProject(String workId, SupervisorProjectDto supervisorProjectDto) {
        SupervisorProject existingSupervisorProject = supervisorProjectRepository.findById(workId).orElse(null);
        if (existingSupervisorProject != null) {
            BeanUtils.copyProperties(supervisorProjectDto, existingSupervisorProject);
            return supervisorProjectRepository.save(existingSupervisorProject);
        }
        return null;
    }
    
    @Override
    public void deleteSupervisorProject(String workId) {
        supervisorProjectRepository.deleteById(workId);
    }
}
