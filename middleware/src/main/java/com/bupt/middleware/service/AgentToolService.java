package com.bupt.middleware.service;

import com.bupt.middleware.common.Result;
import com.bupt.middleware.entity.AgentTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
public class AgentToolService {

    @Value("${database.url}")
    private String databaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Result<List<AgentTool>> getAllActiveTools(String category) {
        try {
            // 调用Database服务的正确路径
            String url = databaseUrl + "/database/agent/tools";
            if (category != null && !category.isEmpty()) {
                url += "?category=" + category;
            }

            ResponseEntity<Result<List<AgentTool>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Result<List<AgentTool>>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            // 调用失败时的备用处理
            return Result.error(500, "调用Database服务失败: " + e.getMessage());
        }
    }

    public Result<AgentTool> getToolById(Long id) {
        try {
            String url = databaseUrl + "/database/agent/tools/" + id;
            ResponseEntity<Result<AgentTool>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Result<AgentTool>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            return Result.error(500, "调用Database服务失败: " + e.getMessage());
        }
    }
}