package com.bupt.middleware.service;

import com.bupt.middleware.entity.SupvWideTable;
import com.bupt.middleware.entity.result.Result;
import com.bupt.middleware.entity.result.ResultDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * @author chenxiao
 * @date 2025/4/8 17:54
 * @description: Supervisor Wide Table Service
 */

@Service
public class SupvWideTableService implements ISupvWideTableService {

    @Value("${database.url}")
    private String databaseUrl;

    // 获取导师宽表数据（By Id）
    @Override
    public Result<SupvWideTable> getSupv(String workId) {
        SupvWideTable supvWideTable;

        try (HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 创建HTTP GET请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/supvWideTable/" + workId))
                    .GET()
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<SupvWideTable> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                supvWideTable = resultDto.getData();
            } else {
                return Result.error(resultDto.getCode(), "输入有误");
            }

        } catch (IOException | InterruptedException e) {
            // 网络通信异常
            return Result.error(501, e.getMessage());
        } catch (Exception e) {
            // workId错误异常
            return Result.error(404, e.getMessage());
        }

        return Result.success(supvWideTable, "success");
    }

    // 获取全部导师宽表数据
    @Override
    public Result<List<SupvWideTable>> getAllSupv() {
        List<SupvWideTable> supvWideTables;

        try (HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 创建HTTP GET请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/supvWideTable"))
                    .GET()
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<List<SupvWideTable>> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                supvWideTables = resultDto.getData();
            } else {
                return Result.error(resultDto.getCode(), resultDto.getMessage());
            }

        } catch (IOException | InterruptedException e) {
            // 网络通信异常
            return Result.error(501, e.getMessage());
        } catch (Exception e) {
            // workId错误异常
            return Result.error(404, e.getMessage());
        }

        return Result.success(supvWideTables, "success");
    }

    // 更新导师宽表数据
    @Override
    public Result<String> updateSupv(SupvWideTable dto) {
        try(HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 构造请求体
            String requestBody = objectMapper.writeValueAsString(dto);

            // 创建HTTP PUT请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/supvWideTable"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<Void> resultDTO = objectMapper.readValue((response.body()), new TypeReference<>() {});

            // 返回结果
            if (resultDTO.getCode() == 200) {
                return Result.success("success");
            } else {
                return Result.error(resultDTO.getCode(), resultDTO.getMessage());
            }
        } catch (JsonProcessingException e) {
            // UserPwd对象转String过程中出错
            return Result.error(501, e.getMessage());
        } catch (IOException | InterruptedException e) {
            // 网络通信异常
            return Result.error(501, e.getMessage());
        } catch (Exception e) {
            // 输入数据错误异常
            return Result.error(404, e.getMessage());
        }
    }
}
