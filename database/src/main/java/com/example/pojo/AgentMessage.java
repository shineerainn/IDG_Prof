package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "agent_message")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private MessageRole role;

    @Column(name = "content", columnDefinition = "LONGTEXT")  // ✅ 添加这个
    private String content;

    @Column(name = "tokens_used")
    private Integer tokensUsed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum MessageRole {
        user, assistant, system
    }

    // getter/setter 保持不变
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