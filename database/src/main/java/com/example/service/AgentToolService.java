package com.example.service;

import com.example.pojo.AgentTool;
import com.example.pojo.dto.AgentToolDto;
import com.example.repository.AgentToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgentToolService {

    @Autowired
    private AgentToolRepository agentToolRepository;

    /**
     * 获取所有激活的工具
     */
    public List<AgentTool> getAllActiveTools() {
        return agentToolRepository.findByIsActiveTrue();
    }

    /**
     * 根据分类获取工具
     */
    public List<AgentTool> getToolsByCategory(String category) {
        return agentToolRepository.findActiveToolsByCategory(category);
    }

    /**
     * 根据ID获取工具
     */
    public Optional<AgentTool> getToolById(Long id) {
        return agentToolRepository.findById(id);
    }

    /**
     * 创建新工具
     */
    public AgentTool createTool(AgentToolDto toolDto) {
        AgentTool tool = new AgentTool();
        tool.setName(toolDto.getName());
        tool.setDescription(toolDto.getDescription());
        tool.setCategory(toolDto.getCategory());
        tool.setSystemPrompt(toolDto.getSystemPrompt());
        tool.setModelName(toolDto.getModelName());
        tool.setIsActive(toolDto.getIsActive());
        tool.setIconUrl(toolDto.getIconUrl());
        tool.setCreatedAt(LocalDateTime.now());
        tool.setUpdatedAt(LocalDateTime.now());

        return agentToolRepository.save(tool);
    }

    /**
     * 更新工具信息
     */
    public AgentTool updateTool(Long id, AgentToolDto toolDto) {
        Optional<AgentTool> optionalTool = agentToolRepository.findById(id);
        if (optionalTool.isPresent()) {
            AgentTool tool = optionalTool.get();
            tool.setName(toolDto.getName());
            tool.setDescription(toolDto.getDescription());
            tool.setCategory(toolDto.getCategory());
            tool.setSystemPrompt(toolDto.getSystemPrompt());
            tool.setModelName(toolDto.getModelName());
            tool.setIsActive(toolDto.getIsActive());
            tool.setIconUrl(toolDto.getIconUrl());
            tool.setUpdatedAt(LocalDateTime.now());

            return agentToolRepository.save(tool);
        }
        return null;
    }

    /**
     * 删除工具
     */
    public boolean deleteTool(Long id) {
        if (agentToolRepository.existsById(id)) {
            agentToolRepository.deleteById(id);
            return true;
        }
        return false;
    }
}