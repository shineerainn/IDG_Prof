package com.bupt.middleware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentMessage {

    private Long id;
    private Long conversationId;
    private MessageRole role;
    private String content;
    private Integer tokensUsed;
    private LocalDateTime createdAt;

    public enum MessageRole {
        user, assistant, system
    }

    // 手动getter/setter方法
    public Long getId() { return id; }
    public Long getConversationId() { return conversationId; }
    public MessageRole getRole() { return role; }
    public String getContent() { return content; }
    public Integer getTokensUsed() { return tokensUsed; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
    public void setRole(MessageRole role) { this.role = role; }
    public void setContent(String content) { this.content = content; }
    public void setTokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}