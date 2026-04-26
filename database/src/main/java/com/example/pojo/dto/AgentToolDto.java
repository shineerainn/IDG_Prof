package com.example.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentToolDto {

    private Long id;

    @NotBlank(message = "工具名称不能为空")
    private String name;

    private String description;

    private String category;

    private String systemPrompt;

    private String modelName;

    @NotNull(message = "是否激活不能为空")
    private Boolean isActive;

    private String iconUrl;

    // 手动getter/setter方法
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getSystemPrompt() { return systemPrompt; }
    public String getModelName() { return modelName; }
    public Boolean getIsActive() { return isActive; }
    public String getIconUrl() { return iconUrl; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
}
