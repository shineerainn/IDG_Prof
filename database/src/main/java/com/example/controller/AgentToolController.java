package com.example.controller;

import com.example.common.ResultDTO;
import com.example.pojo.AgentTool;
import com.example.pojo.dto.AgentToolDto;
import com.example.service.AgentToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/database/agent/tools")
public class AgentToolController {

    @Autowired
    private AgentToolService agentToolService;

    /**
     * 获取所有激活的工具
     */
    @GetMapping
    public ResultDTO<List<AgentTool>> getAllActiveTools(@RequestParam(required = false) String category) {
        try {
            List<AgentTool> tools;
            if (category != null && !category.isEmpty()) {
                tools = agentToolService.getToolsByCategory(category);
            } else {
                tools = agentToolService.getAllActiveTools();
            }
            return ResultDTO.success(tools);
        } catch (Exception e) {
            return ResultDTO.error(500, "获取工具列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取工具详情
     */
    @GetMapping("/{id}")
    public ResultDTO<AgentTool> getToolById(@PathVariable Long id) {
        try {
            Optional<AgentTool> tool = agentToolService.getToolById(id);
            if (tool.isPresent()) {
                return ResultDTO.success(tool.get());
            } else {
                return ResultDTO.error(404, "工具不存在");
            }
        } catch (Exception e) {
            return ResultDTO.error(500, "获取工具详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建新工具
     */
    @PostMapping
    public ResultDTO<AgentTool> createTool(@Valid @RequestBody AgentToolDto toolDto) {
        try {
            AgentTool createdTool = agentToolService.createTool(toolDto);
            return ResultDTO.success(createdTool);
        } catch (Exception e) {
            return ResultDTO.error(500, "创建工具失败: " + e.getMessage());
        }
    }

    /**
     * 更新工具信息
     */
    @PutMapping("/{id}")
    public ResultDTO<AgentTool> updateTool(@PathVariable Long id,
                                           @Valid @RequestBody AgentToolDto toolDto) {
        try {
            AgentTool updatedTool = agentToolService.updateTool(id, toolDto);
            if (updatedTool != null) {
                return ResultDTO.success(updatedTool);
            } else {
                return ResultDTO.error(404, "工具不存在");
            }
        } catch (Exception e) {
            return ResultDTO.error(500, "更新工具失败: " + e.getMessage());
        }
    }

    /**
     * 删除工具
     */
    @DeleteMapping("/{id}")
    public ResultDTO<String> deleteTool(@PathVariable Long id) {
        try {
            boolean deleted = agentToolService.deleteTool(id);
            if (deleted) {
                return ResultDTO.success("删除成功");
            } else {
                return ResultDTO.error(404, "工具不存在");
            }
        } catch (Exception e) {
            return ResultDTO.error(500, "删除工具失败: " + e.getMessage());
        }
    }
}
