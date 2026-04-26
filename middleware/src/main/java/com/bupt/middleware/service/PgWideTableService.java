package com.bupt.middleware.service;

import com.bupt.middleware.entity.PgWideTable;
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
 * @date 2025/4/8 17:20
 * @description: Wide Table Service
 */

@Service
public class PgWideTableService implements IPgWideTableService {

    @Value("${database.url}")
    private String databaseUrl;

    // 获取学生宽表数据（By Id）
    @Override
    public Result<PgWideTable> getPg(String pgId) {
        PgWideTable pgWideTable;

        try (HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 创建HTTP GET请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/pgWideTable/" + pgId))
                    .GET()
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<PgWideTable> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                pgWideTable = resultDto.getData();
            } else {
                return Result.error(resultDto.getCode(), "输入有误");
            }

        } catch (IOException | InterruptedException e) {
            // 网络通信异常
            return Result.error(501, e.getMessage());
        } catch (Exception e) {
            // pgId错误异常
            return Result.error(404, e.getMessage());
        }

        return Result.success(pgWideTable, "success");
    }

    // 获取全部学生宽表数据
    @Override
    public Result<List<PgWideTable>> getAllPg() {
        List<PgWideTable> pgWideTables;

        try (HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 创建HTTP GET请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/pgWideTable"))
                    .GET()
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<List<PgWideTable>> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                pgWideTables = resultDto.getData();
            } else {
                return Result.error(resultDto.getCode(), resultDto.getMessage());
            }

        } catch (IOException | InterruptedException e) {
            // 网络通信异常
            return Result.error(501, e.getMessage());
        } catch (Exception e) {
            // 未找到数据
            return Result.error(404, e.getMessage());
        }

        return Result.success(pgWideTables, "success");
    }

    // 更新学生宽表数据
    @Override
    public Result<String> updatePg(PgWideTable dto) {
        try(HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 构造请求体
            String requestBody = objectMapper.writeValueAsString(dto);

            // 创建HTTP PUT请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/pgWideTable"))
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
            // 对象转String过程中出错
            return Result.error(501, e.getMessage());
        } catch (IOException | InterruptedException e) {
            // 网络通信异常
            return Result.error(501, e.getMessage());
        } catch (Exception e) {
            // 输入数据错误异常
            return Result.error(404, e.getMessage());
        }
    }

    /**
     * 批量更新学生宽表数据
     */
    @Override
    public Result<String> updatePgs(List<PgWideTable> pgWideTables) {
        try(HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 构造请求体
            String requestBody = objectMapper.writeValueAsString(pgWideTables);

            // 创建HTTP PUT请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/pgWideTable/all"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
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
            // 对象转String过程中出错
            return Result.error(501, e.getMessage());
        } catch (IOException | InterruptedException e) {
            // 网络通信异常
            return Result.error(501, e.getMessage());
        } catch (Exception e) {
            // 输入数据错误异常
            return Result.error(404, e.getMessage());
        }    }
}
