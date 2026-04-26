package com.bupt.middleware.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateConversationRequest {

    private String userId;
    private Long toolId;
    private String title;
    private String modelName;

    // 手动getter/setter方法
    public String getUserId() { return userId; }
    public Long getToolId() { return toolId; }
    public String getTitle() { return title; }
    public String getModelName() { return modelName; }

    public void setUserId(String userId) { this.userId = userId; }
    public void setToolId(Long toolId) { this.toolId = toolId; }
    public void setTitle(String title) { this.title = title; }
    public void setModelName(String modelName) { this.modelName = modelName; }
}