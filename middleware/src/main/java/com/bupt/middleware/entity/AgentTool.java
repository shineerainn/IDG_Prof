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
public class AgentTool {

    private Long id;
    private String name;
    private String description;
    private String category;
    private String systemPrompt;
    private String modelName;
    private Boolean isActive;
    private String iconUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AgentTool(long l, String 智能助手, String s, String general, boolean b) {
    }

    // 手动getter/setter方法
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getSystemPrompt() { return systemPrompt; }
    public String getModelName() { return modelName; }
    public Boolean getIsActive() { return isActive; }
    public String getIconUrl() { return iconUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
