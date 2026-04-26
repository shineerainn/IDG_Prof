package com.example.controller;

import com.example.common.ResultDTO;
import com.example.pojo.AgentMessage;
import com.example.pojo.dto.AgentMessageDto;
import com.example.service.AgentMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/database/agent/messages")
public class AgentMessageController {

    @Autowired
    private AgentMessageService messageService;

    /**
     * 获取对话的消息列表
     */
    @GetMapping
    public ResultDTO<List<AgentMessage>> getConversationMessages(
            @RequestParam Long conversationId,
            @RequestParam(defaultValue = "0") int limit) {
        try {
            List<AgentMessage> messages;
            if (limit > 0) {
                messages = messageService.getRecentMessages(conversationId, limit);
            } else {
                messages = messageService.getConversationMessages(conversationId);
            }
            return ResultDTO.success(messages);
        } catch (Exception e) {
            return ResultDTO.error(500, "获取消息列表失败: " + e.getMessage());
        }
    }

    /**
     * 创建新消息
     */
    @PostMapping
    public ResultDTO<AgentMessage> createMessage(@Valid @RequestBody AgentMessageDto messageDto) {
        try {
            AgentMessage createdMessage = messageService.createMessage(messageDto);
            return ResultDTO.success(createdMessage);
        } catch (Exception e) {
            return ResultDTO.error(500, "创建消息失败: " + e.getMessage());
        }
    }

    /**
     * 获取消息统计
     */
    @GetMapping("/stats")
    public ResultDTO<Long> getMessageCount(@RequestParam Long conversationId) {
        try {
            long count = messageService.getMessageCount(conversationId);
            return ResultDTO.success(count);
        } catch (Exception e) {
            return ResultDTO.error(500, "获取消息统计失败: " + e.getMessage());
        }
    }

    /**
     * 删除消息
     */
    @DeleteMapping("/{id}")
    public ResultDTO<String> deleteMessage(@PathVariable Long id) {
        try {
            boolean deleted = messageService.deleteMessage(id);
            if (deleted) {
                return ResultDTO.success("删除成功");
            } else {
                return ResultDTO.error(404, "消息不存在");
            }
        } catch (Exception e) {
            return ResultDTO.error(500, "删除消息失败: " + e.getMessage());
        }
    }
}