package com.bupt.middleware.service;

import com.bupt.middleware.common.Result;
import com.bupt.middleware.common.ResultDTO;
import com.bupt.middleware.entity.AgentMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AgentMessageService {

    @Value("${database.url:http://127.0.0.1:8088}")
    private String databaseUrl;

    /**
     * 获取对话的消息列表
     */
    public Result<List<AgentMessage>> getConversationMessages(Long conversationId) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/agent/messages?conversationId=" + conversationId))
                    .GET()
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            ResultDTO<List<AgentMessage>> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                return Result.success(resultDto.getData());
            } else {
                return Result.error(resultDto.getCode(), resultDto.getMessage());
            }
        } catch (Exception e) {
            log.error("获取消息列表失败: ", e);
            return Result.error(500, "获取消息列表失败: " + e.getMessage());
        }
    }

    /**
     * 创建新消息
     */
    public Result<AgentMessage> createMessage(Long conversationId, AgentMessage.MessageRole role, String content, Integer tokensUsed) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("conversationId", conversationId);
            requestBody.put("role", role.toString());
            requestBody.put("content", content);
            requestBody.put("tokensUsed", tokensUsed);

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/agent/messages"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            ResultDTO<AgentMessage> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                return Result.success(resultDto.getData());
            } else {
                return Result.error(resultDto.getCode(), resultDto.getMessage());
            }
        } catch (Exception e) {
            log.error("创建消息失败: ", e);
            return Result.error(500, "创建消息失败: " + e.getMessage());
        }
    }
}
