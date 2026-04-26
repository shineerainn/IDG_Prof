package com.bupt.middleware.service;

import com.bupt.middleware.common.Result;
import com.bupt.middleware.common.ResultDTO;
import com.bupt.middleware.dto.CreateConversationRequest;
import com.bupt.middleware.entity.AgentConversation;
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
public class AgentConversationService {

    @Value("${database.url:http://127.0.0.1:8088}")
    private String databaseUrl;

    /**
     * 获取用户的对话列表
     */
    public Result<List<AgentConversation>> getUserConversations(String userId, Long toolId) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            String url = databaseUrl + "/database/agent/conversations?userId=" + userId;
            if (toolId != null) {
                url += "&toolId=" + toolId;
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            ResultDTO<List<AgentConversation>> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                return Result.success(resultDto.getData());
            } else {
                return Result.error(resultDto.getCode(), resultDto.getMessage());
            }
        } catch (Exception e) {
            log.error("获取对话列表失败: ", e);
            return Result.error(500, "获取对话列表失败: " + e.getMessage());
        }
    }

    /**
     * 创建新对话
     */
    public Result<AgentConversation> createConversation(CreateConversationRequest request) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userId", request.getUserId());
            requestBody.put("toolId", request.getToolId());
            requestBody.put("title", request.getTitle() != null ? request.getTitle() : "新对话");
            requestBody.put("modelName", request.getModelName() != null ? request.getModelName() : "gpt-4o");

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/agent/conversations"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            HttpResponse<InputStream> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());

            ResultDTO<AgentConversation> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                return Result.success(resultDto.getData());
            } else {
                return Result.error(resultDto.getCode(), resultDto.getMessage());
            }
        } catch (Exception e) {
            log.error("创建对话失败: ", e);
            return Result.error(500, "创建对话失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取对话详情
     */
    public Result<AgentConversation> getConversationById(Long id) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/agent/conversations/" + id))
                    .GET()
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            ResultDTO<AgentConversation> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                return Result.success(resultDto.getData());
            } else {
                return Result.error(resultDto.getCode(), resultDto.getMessage());
            }
        } catch (Exception e) {
            log.error("获取对话详情失败: ", e);
            return Result.error(500, "获取对话详情失败: " + e.getMessage());
        }
    }

    /**
     * 更新对话标题
     */
    public Result<AgentConversation> updateConversationTitle(Long conversationId, String title) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("title", title);

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/agent/conversations/" + conversationId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            ResultDTO<AgentConversation> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                return Result.success(resultDto.getData());
            } else {
                return Result.error(resultDto.getCode(), resultDto.getMessage());
            }
        } catch (Exception e) {
            log.error("更新对话标题失败: ", e);
            return Result.error(500, "更新对话标题失败: " + e.getMessage());
        }
    }

    /**
     * 删除对话
     */
    public Result<String> deleteConversation(Long conversationId) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/agent/conversations/" + conversationId))
                    .DELETE()
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            ResultDTO<String> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                return Result.success("删除成功");
            } else {
                return Result.error(resultDto.getCode(), resultDto.getMessage());
            }
        } catch (Exception e) {
            log.error("删除对话失败: ", e);
            return Result.error(500, "删除对话失败: " + e.getMessage());
        }
    }
}
