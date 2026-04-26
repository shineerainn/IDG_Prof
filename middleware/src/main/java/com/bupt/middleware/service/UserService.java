package com.bupt.middleware.service;

import com.bupt.middleware.entity.UserPwd;
import com.bupt.middleware.entity.UserRole;
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

/**
 * @author chenxiao
 * @date 2025/4/8 18:15
 * @description: User Service
 */

@Service
public class UserService implements IUserService {

    @Value("${database.url}")
    private String databaseUrl;

    @Override
    public Result<UserPwd> getPwd(String userId) {
        String userPassword;

        try (HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();

            // 创建HTTP GET请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/userPwd/" + userId))
                    .GET()
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<UserPwd> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            userPassword = resultDto.getData().getUserPwd();

        } catch (IOException | InterruptedException e) {
            // 网络通信异常
            return Result.error(501, e.getMessage());
        } catch (Exception e) {
            // 用户名错误异常
            return Result.error(404, e.getMessage());
        }

        UserPwd userPwd = UserPwd.builder().userId(userId).userPwd(userPassword).build();

        return Result.success(userPwd, "success");
    }

    @Override
    public Result<String> updatePwd(UserPwd newUserPwd) {
        String userId = newUserPwd.getUserId();
        String newPwd = newUserPwd.getUserPwd();
        UserPwd userPwd = UserPwd.builder().userId(userId).userPwd(newPwd).build();

        try(HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();

            // 构建请求体
            String requestBody = objectMapper.writeValueAsString(userPwd);

            // 创建HTTP PUT请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/userPwd"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<Void> resultDTO = objectMapper.readValue(response.body(), new TypeReference<>() {});

            // 返回结果
            if (resultDTO.getCode() == 200) {
                return Result.success(String.format("Change %s's password!", userId));
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

    @Override
    public Result<String> addUser(UserRole userRole) {
        String userId = userRole.getUserId();
        String newRole = userRole.getUserRole();

        // TODO: 调用数据模块接口增加用户，并判定是否成功（问题：是否新增用户的时候，同时新增密码表和权限表？）
        return Result.success(String.format("Add user %s'!", userId));
    }

    @Override
    public Result<UserRole> getRole(String userId) {
        String role;

        try (HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();

            // 创建HTTP GET请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/userRole/" + userId))
                    .GET()
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<UserRole> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            role = resultDto.getData().getUserRole();

        } catch (IOException | InterruptedException e) {
            // 网络通信异常
            return Result.error(501, e.getMessage());
        } catch (Exception e) {
            // 用户名错误异常
            return Result.error(404, e.getMessage());
        }

        UserRole userRole = UserRole.builder().userId(userId).userRole(role).build();

        return Result.success(userRole, "success");
    }

    @Override
    public Result<String> updateRole(UserRole newUserRole) {
        String userId = newUserRole.getUserId();
        String newRole = newUserRole.getUserRole();
        UserRole userRole = UserRole.builder().userId(userId).userRole(newRole).build();

        try(HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();

            // 构建请求体
            String requestBody = objectMapper.writeValueAsString(userRole);

            // 创建HTTP PUT请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/userRole"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<Void> resultDTO = objectMapper.readValue(response.body(), new TypeReference<>() {});

            // 返回结果
            if (resultDTO.getCode() == 200) {
                return Result.success(String.format("Update user %s's role!", userId));
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
