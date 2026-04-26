package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "agent_conversation")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentConversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "tool_id")
    private Long toolId;

    @Column(name = "title")
    private String title;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "is_pinned")
    private Boolean isPinned;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @Column(name = "total_tokens")
    private Integer totalTokens;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 手动getter/setter方法
    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public Long getToolId() { return toolId; }
    public String getTitle() { return title; }
    public String getModelName() { return modelName; }
    public Boolean getIsPinned() { return isPinned; }
    public Boolean getIsArchived() { return isArchived; }
    public Integer getTotalTokens() { return totalTokens; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setToolId(Long toolId) { this.toolId = toolId; }
    public void setTitle(String title) { this.title = title; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public void setIsPinned(Boolean isPinned) { this.isPinned = isPinned; }
    public void setIsArchived(Boolean isArchived) { this.isArchived = isArchived; }
    public void setTotalTokens(Integer totalTokens) { this.totalTokens = totalTokens; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}