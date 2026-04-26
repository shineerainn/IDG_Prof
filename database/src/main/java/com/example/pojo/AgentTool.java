package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "agent_tool")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "system_prompt")
    private String systemPrompt;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 手动添加getter/setter方法（基于之前的Lombok问题）
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