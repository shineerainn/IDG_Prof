package com.bupt.middleware.service;

import com.bupt.middleware.entity.AlgorithmConfig;
import com.bupt.middleware.entity.SupvProfileRecord;
import com.bupt.middleware.entity.SupvWideTable;
import com.bupt.middleware.entity.result.Result;
import com.bupt.middleware.entity.result.ResultDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenxiao
 * @date 2025/4/9 16:46
 * @description: Supervisor Profile Cluster Service
 */

@Service
public class SupvProfileService implements ISupvProfileService {

    @Value("${database.url}")
    private String databaseUrl;

    /**
     * 聚类生成导师画像
     * key = "clusterResult"
     */
    @Override
    public List<Map<String, Object>> buildSupvProfile(List<String> attrList, List<SupvWideTable> supvWideTables, AlgorithmConfig algorithmConfig) {
        // 数据转json
        // List<PgWideTable> 转 List<Map<String, Object>>
        List<Map<String, Object>> data = tableToMap(supvWideTables);

        // 交由AI计算模块进行分类汇总
        // 选择接口路径
        String url;
        List<Map<String, Object>>res = List.of();

        // 构建请求体（JSON格式）
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("target_columns",attrList);
        requestBody.put("algo",Map.of(
                "profileAlgorithm", "kMeans",
                "detectionAlgorithm", "id3Tree",
                "kMeans", Map.of("nClusters", 8, "init","k-means++", "maxIter", 300,
                        "nInit", 4, "tol", 0.0001, "randomState", 0,
                        "algorithm", "auto", "copyX", true, "nJobs", 0),
                "id3Tree", Map.of("maxDepth", "None", "minSampleSplit",2, "minSampleLeaf",1))
        );

        url = "http://127.0.0.1:8009/cluster/buildTeacherCluster";
        requestBody.put("teacher_wide_table",data);
        res = requestForwarding(requestBody,url);

        // 返回数据
        return res;
    }

    /**
     * 画像列表查询
     */
    @Override
    public Result<List<SupvProfileRecord>> getSupvProfileRecords(String userId) {
        List<SupvProfileRecord> supvProfileRecords;

        try (HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 创建HTTP GET请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/supvProfile/userId/" + userId))
                    .GET()
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<List<SupvProfileRecord>> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                supvProfileRecords = resultDto.getData();
            } else {
                return Result.error(resultDto.getCode(), resultDto.getMessage());
            }

        } catch (IOException | InterruptedException e) {
            // 网络通信异常
            return Result.error(501, e.getMessage());
        } catch (Exception e) {
            // pgId错误异常
            return Result.error(404, e.getMessage());
        }

        return Result.success(supvProfileRecords, "success");
    }

    /**
     * 画像Id查询记录
     */
    @Override
    public Result<SupvProfileRecord> getSupvProfileRecord(String profileId) {
        SupvProfileRecord supvProfileRecord;

        try (HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 创建HTTP GET请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/supvProfile/profileId/" + profileId))
                    .GET()
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<SupvProfileRecord> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                supvProfileRecord = resultDto.getData();
            } else {
                return Result.error(resultDto.getCode(), resultDto.getMessage());
            }

        } catch (IOException | InterruptedException e) {
            // 网络通信异常
            return Result.error(501, e.getMessage());
        } catch (Exception e) {
            // pgId错误异常
            return Result.error(404, e.getMessage());
        }

        return Result.success(supvProfileRecord, "success");
    }

    /**
     * 画像记录存储
     */
    @Override
    public Result<String> postSupvProfileRecord(SupvProfileRecord supvProfileRecord) {
        try(HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 构造请求体
            String requestBody = objectMapper.writeValueAsString(supvProfileRecord);

            // 创建HTTP PUT请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/supvProfile"))
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
        }
    }

    // 包装转发函数
    public List<Map<String,Object>> requestForwarding(Map<String, Object> requestBody,String url){
        // 向python后端发送post请求，获取数据

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 发送请求并获取响应
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // 处理python返回内容
        String jsonResponse = response.getBody();
        Gson gson = new Gson();
        Map<String, Object> temp = gson.fromJson(
                jsonResponse,
                new TypeToken<Map<String, Object>>(){}.getType()
        );
        List<Map<String, Object>> wideTable = gson.fromJson(
                temp.get("data").toString(),
                new TypeToken<List<Map<String, Object>>>(){}.getType()
        );
        return wideTable;
    }

    // 宽表转Map格式
    public <T> List<Map<String,Object>> tableToMap(List<T> tables){
        List<Map<String,Object>> data = new ArrayList<>();
        for(T table:tables){
            data.add(bean2Map(table));
        }
        return data;
    }

    // Bean转为Map
    public static Map<String, Object> bean2Map(Object object) {
        Map<String, Object> map = new HashMap<>();
        ReflectionUtils.doWithFields(object.getClass(), field -> {
            field.setAccessible(true);
            Object value = ReflectionUtils.getField(field, object);
            if (value != null) {
                map.put(field.getName(), value);
            }
        });
        return map;
    }
}
