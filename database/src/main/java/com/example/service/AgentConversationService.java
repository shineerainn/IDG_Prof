package com.example.service;

import com.example.pojo.AgentConversation;
import com.example.pojo.dto.AgentConversationDto;
import com.example.repository.AgentConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgentConversationService {

    @Autowired
    private AgentConversationRepository conversationRepository;

    /**
     * 获取用户的所有对话
     */
    public List<AgentConversation> getUserConversations(String userId) {
        return conversationRepository.findActiveConversationsByUser(userId);
    }

    /**
     * 获取用户在特定工具下的对话
     */
    public List<AgentConversation> getUserConversationsByTool(String userId, Long toolId) {
        return conversationRepository.findByUserIdAndToolIdOrderByUpdatedAtDesc(userId, toolId);
    }

    /**
     * 根据ID获取对话
     */
    public Optional<AgentConversation> getConversationById(Long id) {
        return conversationRepository.findById(id);
    }

    /**
     * 创建新对话
     */
    public AgentConversation createConversation(AgentConversationDto conversationDto) {
        AgentConversation conversation = new AgentConversation();
        conversation.setUserId(conversationDto.getUserId());
        conversation.setToolId(conversationDto.getToolId());
        conversation.setTitle(conversationDto.getTitle());
        conversation.setModelName(conversationDto.getModelName());
        conversation.setIsPinned(false);
        conversation.setIsArchived(false);
        conversation.setTotalTokens(0);
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setUpdatedAt(LocalDateTime.now());

        return conversationRepository.save(conversation);
    }

    /**
     * 更新对话信息
     */
    public AgentConversation updateConversation(Long id, AgentConversationDto conversationDto) {
        Optional<AgentConversation> optionalConversation = conversationRepository.findById(id);
        if (optionalConversation.isPresent()) {
            AgentConversation conversation = optionalConversation.get();
            if (conversationDto.getTitle() != null) {
                conversation.setTitle(conversationDto.getTitle());
            }
            if (conversationDto.getModelName() != null) {
                conversation.setModelName(conversationDto.getModelName());
            }
            if (conversationDto.getIsPinned() != null) {
                conversation.setIsPinned(conversationDto.getIsPinned());
            }
            if (conversationDto.getIsArchived() != null) {
                conversation.setIsArchived(conversationDto.getIsArchived());
            }
            conversation.setUpdatedAt(LocalDateTime.now());

            return conversationRepository.save(conversation);
        }
        return null;
    }

    /**
     * 更新对话的token使用量
     */
    public void updateConversationTokens(Long conversationId, Integer tokens) {
        Optional<AgentConversation> optionalConversation = conversationRepository.findById(conversationId);
        if (optionalConversation.isPresent()) {
            AgentConversation conversation = optionalConversation.get();
            conversation.setTotalTokens(conversation.getTotalTokens() + tokens);
            conversation.setUpdatedAt(LocalDateTime.now());
            conversationRepository.save(conversation);
        }
    }

    /**
     * 删除对话
     */
    public boolean deleteConversation(Long id) {
        if (conversationRepository.existsById(id)) {
            conversationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}