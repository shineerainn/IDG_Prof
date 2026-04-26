package com.example.pojo.dto;

import com.example.pojo.AgentMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentMessageDto {

    private Long id;

    @NotNull(message = "对话ID不能为空")
    private Long conversationId;

    @NotNull(message = "消息角色不能为空")
    private AgentMessage.MessageRole role;

    @NotBlank(message = "消息内容不能为空")
    private String content;

    private Integer tokensUsed;

    private LocalDateTime createdAt;

    // 手动getter/setter方法
    public Long getId() { return id; }
    public Long getConversationId() { return conversationId; }
    public AgentMessage.MessageRole getRole() { return role; }
    public String getContent() { return content; }
    public Integer getTokensUsed() { return tokensUsed; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
    public void setRole(AgentMessage.MessageRole role) { this.role = role; }
    public void setContent(String content) { this.content = content; }
    public void setTokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}