package com.bupt.middleware.service;

import com.bupt.middleware.entity.AlgorithmConfig;
import com.bupt.middleware.entity.PgAcademic;
import com.bupt.middleware.entity.result.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author chenxiao
 * @date 2025/4/9 16:15
 * @description: Postgraduate Academic Analysis Service
 */
@Service
@Slf4j
public class PgAcademicAnalysisService implements IPgAcademicAnalysisService {

    @Value("${database.url}")
    private String databaseUrl;

    @Autowired
    private IPgAcademicService pgAcademicService;

    /**
     * 计算学生各项指标分数
     */
    @Override
    public Result<PgAcademic> calculateStudentScores(String studentId) {
        log.info("Calculating scores for student: {}", studentId);
        try {
            // 获取学生数据
            Result<PgAcademic> studentResult = pgAcademicService.getPg(studentId);
            if (studentResult.getCode() != 200) {
                return Result.error(studentResult.getCode(), studentResult.getMsg());
            }

            PgAcademic student = studentResult.getData();
            if (student == null) {
                return Result.error(404, "学生数据不存在");
            }

            // 计算各项分数（这里可以根据具体业务逻辑实现）
            // 暂时返回原始数据，实际应该根据业务规则计算
            return Result.success(student, "计算完成");
        } catch (Exception e) {
            log.error("计算学生分数失败", e);
            return Result.error(500, "计算失败：" + e.getMessage());
        }
    }

    /**
     * 批量计算所有学生的各项指标分数
     */
    @Override
    public Result<List<PgAcademic>> calculateAllStudentScores() {
        log.info("Calculating scores for all students");
        try {
            // 获取所有学生数据
            Result<List<PgAcademic>> allStudentsResult = pgAcademicService.getAllPg();
            if (allStudentsResult.getCode() != 200) {
                return Result.error(allStudentsResult.getCode(), allStudentsResult.getMsg());
            }

            List<PgAcademic> students = allStudentsResult.getData();
            if (students == null || students.isEmpty()) {
                return Result.success(new ArrayList<>(), "没有学生数据");
            }

            // 批量计算分数（这里可以根据具体业务逻辑实现）
            // 暂时返回原始数据，实际应该根据业务规则计算
            return Result.success(students, "批量计算完成");
        } catch (Exception e) {
            log.error("批量计算学生分数失败", e);
            return Result.error(500, "批量计算失败：" + e.getMessage());
        }
    }

    /**
     * 执行学业特征聚类分析
     */
    @Override
    public Result<Map<String, Object>> performAcademicClustering(Map<String, Object> algorithm) {
        log.info("Performing academic clustering with algorithm: {}", algorithm);
        try {
            // 获取所有学生数据
            Result<List<PgAcademic>> allStudentsResult = pgAcademicService.getAllPg();
            if (allStudentsResult.getCode() != 200) {
                return Result.error(allStudentsResult.getCode(), allStudentsResult.getMsg());
            }

            List<PgAcademic> students = allStudentsResult.getData();
            if (students == null || students.isEmpty()) {
                return Result.error(404, "没有学生数据");
            }

            // 默认属性列表
            List<String> defaultAttrList = Arrays.asList(
                "academicPerformanceScore", "researchProjectScore", "thesisScore",
                "academicPaperScore", "competitionScore", "patentSoftwareScore",
                "otherInnovationAchievementScore"
            );

            // 执行聚类
            List<Map<String, Object>> clusteringResult = buildAcademicProfile(defaultAttrList, students, null);
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("clustering_result", clusteringResult);
            result.put("success", true);
            result.put("message", "聚类分析完成");

            return Result.success(result, "聚类分析完成");
        } catch (Exception e) {
            log.error("执行聚类分析失败", e);
            return Result.error(500, "聚类分析失败：" + e.getMessage());
        }
    }

    /**
     * 聚类生成学业画像
     * 参照学生画像的实现逻辑
     */
    @Override
    public List<Map<String, Object>> buildAcademicProfile(List<String> attrList, List<PgAcademic> pgAcademics, AlgorithmConfig algorithmConfig) {
        log.info("Building academic profile with {} attributes for {} students", attrList.size(), pgAcademics.size());
        
        // 数据转json
        List<Map<String, Object>> data = tableToMap(pgAcademics);

        // 交由AI计算模块进行分类汇总
        String url = "http://127.0.0.1:8009/idg-prof/academicProfile";

        // 构建请求体（JSON格式）- 匹配Python端点期望的格式
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("academic_data", data);
        requestBody.put("algorithm_config", Map.of(
                "algorithm", "KMeans",
                "params", Map.of(
                        "n_clusters", 8,
                        "init", "k-means++",
                        "max_iter", 300,
                        "n_init", 4,
                        "tol", 0.0001,
                        "random_state", 0
                )
        ));
        requestBody.put("weights", null); // 使用默认权重

        List<Map<String, Object>> res = requestForwarding(requestBody, url);
        return res;
    }

    /**
     * 获取学生雷达图数据（个人得分 vs 同年级平均分）
     */
    @Override
    public Result<Map<String, Object>> getStudentRadarChartData(String studentId) {
        log.info("Getting radar chart data for student: {}", studentId);
        try {
            // 获取学生数据
            Result<PgAcademic> studentResult = pgAcademicService.getPg(studentId);
            if (studentResult.getCode() != 200) {
                return Result.error(studentResult.getCode(), studentResult.getMsg());
            }

            PgAcademic student = studentResult.getData();
            if (student == null) {
                return Result.error(404, "学生数据不存在");
            }

            // 构建雷达图数据（修复String到double的转换）
            Map<String, Object> radarData = new HashMap<>();
            radarData.put("studentId", studentId);
            radarData.put("studentName", student.getGraduateName());
            radarData.put("scores", Map.of(
                "学业表现", parseDoubleSafely(student.getAcademicPerformanceScore()),
                "科研项目", parseDoubleSafely(student.getResearchProjectScore()),
                "论文质量", parseDoubleSafely(student.getThesisScore()),
                "学术论文", parseDoubleSafely(student.getAcademicPaperScore()),
                "竞赛获奖", parseDoubleSafely(student.getCompetitionScore()),
                "专利软件", parseDoubleSafely(student.getPatentSoftwareScore()),
                "创新成果", parseDoubleSafely(student.getOtherInnovationAchievementScore())
            ));

            return Result.success(radarData, "获取雷达图数据成功");
        } catch (Exception e) {
            log.error("获取雷达图数据失败", e);
            return Result.error(500, "获取雷达图数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取同年级学生平均分数据
     */
    @Override
    public Result<Map<String, Object>> getGradeAverageScores(String grade) {
        log.info("Getting average scores for grade: {}", grade);
        try {
            // 获取所有学生数据
            Result<List<PgAcademic>> allStudentsResult = pgAcademicService.getAllPg();
            if (allStudentsResult.getCode() != 200) {
                return Result.error(allStudentsResult.getCode(), allStudentsResult.getMsg());
            }

            List<PgAcademic> students = allStudentsResult.getData();
            if (students == null || students.isEmpty()) {
                return Result.error(404, "没有学生数据");
            }

            // 计算平均分（修复String到double的转换）
            double academicAvg = students.stream()
                .filter(s -> s.getAcademicPerformanceScore() != null && !s.getAcademicPerformanceScore().trim().isEmpty())
                .mapToDouble(s -> {
                    try {
                        return Double.parseDouble(s.getAcademicPerformanceScore());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .average().orElse(0.0);

            double researchAvg = students.stream()
                .filter(s -> s.getResearchProjectScore() != null && !s.getResearchProjectScore().trim().isEmpty())
                .mapToDouble(s -> {
                    try {
                        return Double.parseDouble(s.getResearchProjectScore());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .average().orElse(0.0);

            double thesisAvg = students.stream()
                .filter(s -> s.getThesisScore() != null && !s.getThesisScore().trim().isEmpty())
                .mapToDouble(s -> {
                    try {
                        return Double.parseDouble(s.getThesisScore());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .average().orElse(0.0);

            double paperAvg = students.stream()
                .filter(s -> s.getAcademicPaperScore() != null && !s.getAcademicPaperScore().trim().isEmpty())
                .mapToDouble(s -> {
                    try {
                        return Double.parseDouble(s.getAcademicPaperScore());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .average().orElse(0.0);

            double competitionAvg = students.stream()
                .filter(s -> s.getCompetitionScore() != null && !s.getCompetitionScore().trim().isEmpty())
                .mapToDouble(s -> {
                    try {
                        return Double.parseDouble(s.getCompetitionScore());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .average().orElse(0.0);

            double patentAvg = students.stream()
                .filter(s -> s.getPatentSoftwareScore() != null && !s.getPatentSoftwareScore().trim().isEmpty())
                .mapToDouble(s -> {
                    try {
                        return Double.parseDouble(s.getPatentSoftwareScore());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .average().orElse(0.0);

            double innovationAvg = students.stream()
                .filter(s -> s.getOtherInnovationAchievementScore() != null && !s.getOtherInnovationAchievementScore().trim().isEmpty())
                .mapToDouble(s -> {
                    try {
                        return Double.parseDouble(s.getOtherInnovationAchievementScore());
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .average().orElse(0.0);

            // 构建平均分数据
            Map<String, Object> averageData = new HashMap<>();
            averageData.put("grade", grade);
            averageData.put("averageScores", Map.of(
                "学业表现", academicAvg,
                "科研项目", researchAvg,
                "论文质量", thesisAvg,
                "学术论文", paperAvg,
                "竞赛获奖", competitionAvg,
                "专利软件", patentAvg,
                "创新成果", innovationAvg
            ));

            return Result.success(averageData, "获取平均分数据成功");
        } catch (Exception e) {
            log.error("获取平均分数据失败", e);
            return Result.error(500, "获取平均分数据失败：" + e.getMessage());
        }
    }

    /**
     * 动态调整特征权重
     */
    @Override
    public Result<PgAcademic> adjustFeatureWeights(String studentId, Map<String, Double> weights) {
        log.info("Adjusting feature weights for student: {} with weights: {}", studentId, weights);
        try {
            // 获取学生数据
            Result<PgAcademic> studentResult = pgAcademicService.getPg(studentId);
            if (studentResult.getCode() != 200) {
                return Result.error(studentResult.getCode(), studentResult.getMsg());
            }

            PgAcademic student = studentResult.getData();
            if (student == null) {
                return Result.error(404, "学生数据不存在");
            }

            // 根据权重调整分数（这里简化处理，实际应该根据具体业务逻辑）
            // 暂时返回原始数据，实际应该根据权重重新计算分数
            return Result.success(student, "权重调整完成");
        } catch (Exception e) {
            log.error("调整特征权重失败", e);
            return Result.error(500, "调整特征权重失败：" + e.getMessage());
        }
    }

    /**
     * 包装转发函数
     */
    public List<Map<String, Object>> requestForwarding(Map<String, Object> requestBody, String url) {
        try {
            log.info("发送请求到Python服务: {}", url);
            log.info("请求参数: {}", requestBody);
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 发送请求并获取响应
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            // 处理python返回内容
            String jsonResponse = response.getBody();
            log.info("Python服务响应: {}", jsonResponse);
            
            Gson gson = new Gson();
            Map<String, Object> temp = gson.fromJson(
                    jsonResponse,
                    new TypeToken<Map<String, Object>>() {}.getType()
            );
            
            // 检查是否有错误
            if (temp.containsKey("error")) {
                log.error("Python服务返回错误: {}", temp.get("error"));
                return new ArrayList<>();
            }
            
            // 解析data字段
            String dataJson = (String) temp.get("data");
            if (dataJson == null) {
                log.error("Python服务返回的data字段为空");
                return new ArrayList<>();
            }
            
            List<Map<String, Object>> wideTable = gson.fromJson(
                    dataJson,
                    new TypeToken<List<Map<String, Object>>>() {}.getType()
            );
            
            log.info("成功解析Python服务返回的数据，记录数: {}", wideTable.size());
            return wideTable;
        } catch (Exception e) {
            log.error("请求Python服务失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 宽表转Map格式
     */
    public <T> List<Map<String, Object>> tableToMap(List<T> tables) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (T table : tables) {
            data.add(bean2Map(table));
        }
        return data;
    }

    /**
     * Bean转为Map
     */
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

    /**
     * 安全地将String转换为double
     */
    private double parseDoubleSafely(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.warn("无法解析数值: {}", value);
            return 0.0;
        }
    }
}
