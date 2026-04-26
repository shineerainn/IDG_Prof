package com.bupt.middleware.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {

    private String content;
    private String modelName;
    private String identity;
    private Map<String, Object> profileState;
    private String conversationContext;

    public String getContent() { return content; }
    public String getModelName() { return modelName; }
    public String getIdentity() { return identity; }
    public Map<String, Object> getProfileState() { return profileState; }
    public String getConversationContext() { return conversationContext; }

    public void setContent(String content) { this.content = content; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public void setIdentity(String identity) { this.identity = identity; }
    public void setProfileState(Map<String, Object> profileState) { this.profileState = profileState; }
    public void setConversationContext(String conversationContext) { this.conversationContext = conversationContext; }
}
