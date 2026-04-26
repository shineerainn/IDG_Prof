package com.example.service;

import com.example.pojo.AgentMessage;
import com.example.pojo.dto.AgentMessageDto;
import com.example.repository.AgentMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgentMessageService {

    @Autowired
    private AgentMessageRepository messageRepository;

    @Autowired
    private AgentConversationService conversationService;

    /**
     * 获取对话的所有消息
     */
    public List<AgentMessage> getConversationMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    /**
     * 获取对话的最新消息（分页）
     */
    public List<AgentMessage> getRecentMessages(Long conversationId, int limit) {
        return messageRepository.findByConversationIdOrderByCreatedAtDesc(
                conversationId, PageRequest.of(0, limit));
    }

    /**
     * 创建新消息
     */
    public AgentMessage createMessage(AgentMessageDto messageDto) {
        AgentMessage message = new AgentMessage();
        message.setConversationId(messageDto.getConversationId());
        message.setRole(messageDto.getRole());
        message.setContent(messageDto.getContent());
        message.setTokensUsed(messageDto.getTokensUsed());
        message.setCreatedAt(LocalDateTime.now());

        AgentMessage savedMessage = messageRepository.save(message);

        // 更新对话的token使用量
        if (messageDto.getTokensUsed() != null) {
            conversationService.updateConversationTokens(
                    messageDto.getConversationId(), messageDto.getTokensUsed());
        }

        return savedMessage;
    }

    /**
     * 获取消息总数
     */
    public long getMessageCount(Long conversationId) {
        return messageRepository.countByConversationId(conversationId);
    }

    /**
     * 删除消息
     */
    public boolean deleteMessage(Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        }
        return false;
    }
}