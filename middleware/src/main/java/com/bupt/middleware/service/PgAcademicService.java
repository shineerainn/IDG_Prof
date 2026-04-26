package com.bupt.middleware.service;

import com.bupt.middleware.entity.PgAcademic;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



@Service
public class PgAcademicService implements IPgAcademicService {

    @Value("${database.url}")
    private String databaseUrl;

    // 获取学生学业数据（By StudentId）
    @Override
    public Result<PgAcademic> getPg(String studentId) {
        PgAcademic pgAcademic;

        try (HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 创建HTTP GET请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/pgAcademic/" + studentId))
                    .GET()
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<PgAcademic> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                pgAcademic = resultDto.getData();
            } else {
                return Result.error(resultDto.getCode(), "输入有误");
            }

        } catch (IOException | InterruptedException e) {
            // 网络通信异常
            return Result.error(501, e.getMessage());
        } catch (Exception e) {
            // studentId错误异常
            return Result.error(404, e.getMessage());
        }

        return Result.success(pgAcademic, "success");
    }

    // 获取全部学生学业数据
    @Override
    public Result<List<PgAcademic>> getAllPg() {
        List<PgAcademic> pgAcademics;

        try (HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 创建HTTP GET请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/pgAcademic"))
                    .GET()
                    .build();

            // 发送请求并获取响应
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // 解析响应
            ResultDTO<List<PgAcademic>> resultDto = objectMapper.readValue(response.body(), new TypeReference<>() {});

            if (resultDto.getCode() == 200) {
                pgAcademics = resultDto.getData();
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

        return Result.success(pgAcademics, "success");
    }

    // 更新学生学业数据
    @Override
    public Result<String> updatePg(PgAcademic dto) {
        try(HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 构造请求体
            String requestBody = objectMapper.writeValueAsString(dto);

            // 创建HTTP PUT请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/pgAcademic"))
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
     * 批量更新学生学业数据
     */
    @Override
    public Result<String> updatePgs(List<PgAcademic> pgAcademics) {
        try(HttpClient client = HttpClient.newHttpClient()) {
            // Jackson JSON 解析器
            ObjectMapper objectMapper = new ObjectMapper();
            // 查找并自动注册Jackson模块，用于支持DateTime的序列化
            objectMapper.findAndRegisterModules();

            // 将PgAcademic转换为PgAcademicDto
            List<Map<String, Object>> dtoList = new ArrayList<>();
            for (PgAcademic academic : pgAcademics) {
                Map<String, Object> dto = new HashMap<>();
                dto.put("studentId", academic.getStudentId());
                dto.put("graduateName", academic.getGraduateName());
                dto.put("weightedAvgGrade", academic.getWeightedAvgGrade());
                dto.put("coreCourseGrade", academic.getCoreCourseGrade());
                dto.put("lastSemesterAvgGrade", academic.getLastSemesterAvgGrade());
                dto.put("gpaGrowthRate", academic.getGpaGrowthRate());
                dto.put("gradeRanking", academic.getGradeRanking());
                dto.put("rankingLevel", academic.getRankingLevel());
                dto.put("academicPerformanceScore", academic.getAcademicPerformanceScore());
                dto.put("researchProjectLevel", academic.getResearchProjectLevel());
                dto.put("projectRole", academic.getProjectRole());
                dto.put("projectAchievementTransformation", academic.getProjectAchievementTransformation());
                dto.put("researchProjectScore", academic.getResearchProjectScore());
                dto.put("thesisDefenseGrade", academic.getThesisDefenseGrade());
                dto.put("thesisInnovativeness", academic.getThesisInnovativeness());
                dto.put("thesisPracticalValue", academic.getThesisPracticalValue());
                dto.put("thesisAchievementTransformation", academic.getThesisAchievementTransformation());
                dto.put("thesisScore", academic.getThesisScore());
                dto.put("publishedJournalConference", academic.getPublishedJournalConference());
                dto.put("authorLevel", academic.getAuthorLevel());
                dto.put("isHighlyCited", academic.getIsHighlyCited());
                dto.put("isIfAbove", academic.getIsIfAbove());
                dto.put("isCoverPaper", academic.getIsCoverPaper());
                dto.put("academicPaperScore", academic.getAcademicPaperScore());
                dto.put("competitionLevel", academic.getCompetitionLevel());
                dto.put("awardLevel", academic.getAwardLevel());
                dto.put("competitionScope", academic.getCompetitionScope());
                dto.put("competitionTeamRole", academic.getCompetitionTeamRole());
                dto.put("competitionScore", academic.getCompetitionScore());
                dto.put("patentSoftwareType", academic.getPatentSoftwareType());
                dto.put("legalStatusCoefficient", academic.getLegalStatusCoefficient());
                dto.put("technologyTransferAmount", academic.getTechnologyTransferAmount());
                dto.put("patentSoftwareScore", academic.getPatentSoftwareScore());
                dto.put("hasStartupProject", academic.getHasStartupProject());
                dto.put("startupFundingStage", academic.getStartupFundingStage());
                dto.put("isCompanyRegistered", academic.getIsCompanyRegistered());
                dto.put("hasStartupCompetitionAward", academic.getHasStartupCompetitionAward());
                dto.put("techApplicationEnterpriseCount", academic.getTechApplicationEnterpriseCount());
                dto.put("techPromotionContractAmount", academic.getTechPromotionContractAmount());
                dto.put("otherInnovationAchievementScore", academic.getOtherInnovationAchievementScore());
                dtoList.add(dto);
            }

            // 构造请求体
            String requestBody = objectMapper.writeValueAsString(dtoList);

            // 创建HTTP PUT请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(databaseUrl + "/database/pgAcademic/batch"))
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
}
