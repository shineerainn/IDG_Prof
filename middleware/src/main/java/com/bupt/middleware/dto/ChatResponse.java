package com.bupt.middleware.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {

    private String content;
    private Integer tokensUsed;
    private String model;

    // 手动getter/setter方法
    public String getContent() { return content; }
    public Integer getTokensUsed() { return tokensUsed; }
    public String getModel() { return model; }

    public void setContent(String content) { this.content = content; }
    public void setTokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; }
    public void setModel(String model) { this.model = model; }
}