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
public class AgentConversation {

    private Long id;
    private String userId;
    private Long toolId;
    private String title;
    private String modelName;
    private Boolean isPinned;
    private Boolean isArchived;
    private Integer totalTokens;
    private LocalDateTime createdAt;
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