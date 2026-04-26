package com.example.controller;

import com.example.common.ResultDTO;
import com.example.pojo.AgentConversation;
import com.example.pojo.dto.AgentConversationDto;
import com.example.service.AgentConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/database/agent/conversations")
public class AgentConversationController {

    @Autowired
    private AgentConversationService conversationService;

    /**
     * 获取用户的对话列表
     */
    @GetMapping
    public ResultDTO<List<AgentConversation>> getUserConversations(
            @RequestParam String userId,
            @RequestParam(required = false) Long toolId) {
        try {
            List<AgentConversation> conversations;
            if (toolId != null) {
                conversations = conversationService.getUserConversationsByTool(userId, toolId);
            } else {
                conversations = conversationService.getUserConversations(userId);
            }
            return ResultDTO.success(conversations);
        } catch (Exception e) {
            return ResultDTO.error(500, "获取对话列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取对话详情
     */
    @GetMapping("/{id}")
    public ResultDTO<AgentConversation> getConversationById(@PathVariable Long id) {
        try {
            Optional<AgentConversation> conversation = conversationService.getConversationById(id);
            if (conversation.isPresent()) {
                return ResultDTO.success(conversation.get());
            } else {
                return ResultDTO.error(404, "对话不存在");
            }
        } catch (Exception e) {
            return ResultDTO.error(500, "获取对话详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建新对话
     */
    @PostMapping
    public ResultDTO<AgentConversation> createConversation(@Valid @RequestBody AgentConversationDto conversationDto) {
        try {
            AgentConversation createdConversation = conversationService.createConversation(conversationDto);
            return ResultDTO.success(createdConversation);
        } catch (Exception e) {
            return ResultDTO.error(500, "创建对话失败: " + e.getMessage());
        }
    }

    /**
     * 更新对话信息
     */
    @PutMapping("/{id}")
    public ResultDTO<AgentConversation> updateConversation(@PathVariable Long id,
                                                           @RequestBody AgentConversationDto conversationDto) {
        try {
            AgentConversation updatedConversation = conversationService.updateConversation(id, conversationDto);
            if (updatedConversation != null) {
                return ResultDTO.success(updatedConversation);
            } else {
                return ResultDTO.error(404, "对话不存在");
            }
        } catch (Exception e) {
            return ResultDTO.error(500, "更新对话失败: " + e.getMessage());
        }
    }

    /**
     * 删除对话
     */
    @DeleteMapping("/{id}")
    public ResultDTO<String> deleteConversation(@PathVariable Long id) {
        try {
            boolean deleted = conversationService.deleteConversation(id);
            if (deleted) {
                return ResultDTO.success("删除成功");
            } else {
                return ResultDTO.error(404, "对话不存在");
            }
        } catch (Exception e) {
            return ResultDTO.error(500, "删除对话失败: " + e.getMessage());
        }
    }
}
