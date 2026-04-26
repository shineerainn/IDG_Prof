package com.bupt.middleware.controller;

import com.bupt.middleware.entity.PgAcademic;
import com.bupt.middleware.entity.result.Result;
import com.bupt.middleware.service.IPgAcademicAnalysisService;
import com.bupt.middleware.service.IPgAcademicService;
import com.bupt.middleware.utils.ModelName;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author chenxiao
 * @date 2025/4/9 11:39
 * @description: Postgraduate Academic Analysis Controller
 */
@RestController
@RequestMapping("/academic/pg")
@Slf4j
public class PgAcademicAnalysisController {

    @Autowired
    private IPgAcademicAnalysisService pgAcademicAnalysisService;

    @Autowired
    private IPgAcademicService pgAcademicService;

    /**
     * 生成学业画像建模（全过程）
     * 参照学生画像的实现逻辑
     */
    @PostMapping("/build")
    public Result<String> buildModel(@RequestBody Map<String, Object> requestData) {
        log.info("Starting to build academic profile model");

        String userId = (String) requestData.get("userId");
        List<String> attrList = (List<String>) requestData.get("attrList");

        if (userId == null || attrList == null || attrList.isEmpty()) {
            return Result.error(400, "用户ID和属性列表不能为空");
        }

        try {
            // 获取全部学业数据 - 修复方法名
            log.debug("Fetching all academic data");
            List<PgAcademic> pgAcademics = pgAcademicService.getAllPg().getData();
            log.info("Fetched {} academic records for clustering", pgAcademics.size());

            // 调用聚类build接口
            log.info("Starting clustering process with {} attributes", attrList.size());
            List<Map<String, Object>> resultList = pgAcademicAnalysisService.buildAcademicProfile(attrList, pgAcademics, null);
            log.info("Clustering completed. Result contains {} records", resultList.size());

            // 确定命名 academic_功能类型_日期_时间
            String profileId = ModelName.getModelName("academic", "profile");
            log.info("Generated profile ID: {}", profileId);

            // 画像结果存文件
            String rootPath = System.getProperty("user.dir");
            String relativePath = "middleware" + File.separator + "src" + File.separator + "main"
                    + File.separator + "java" + File.separator + "out" + File.separator + "academic" + File.separator;
            String savePath = rootPath + File.separator + relativePath;
            log.debug("Preparing to save profile to: {}", savePath);

            File file = new File(savePath);
            if (file.mkdirs()) {
                log.info("Created directory: {}", savePath);
            }

            // 构建完整的建模结果对象
            Map<String, Object> modelingResult = new HashMap<>();
            modelingResult.put("profileId", profileId);
            modelingResult.put("userId", userId);
            modelingResult.put("createTime", LocalDateTime.now().toString());
            modelingResult.put("attrList", attrList);
            modelingResult.put("recordCount", resultList.size());
            modelingResult.put("data", resultList);
            
            // 添加聚类统计信息
            Map<String, Integer> clusterStats = new HashMap<>();
            for (Map<String, Object> record : resultList) {
                Object clusterResult = record.get("clusterResult");
                if (clusterResult != null) {
                    String clusterKey = "cluster_" + clusterResult.toString();
                    clusterStats.put(clusterKey, clusterStats.getOrDefault(clusterKey, 0) + 1);
                }
            }
            modelingResult.put("clusterStats", clusterStats);
            modelingResult.put("totalClusters", clusterStats.size());

            Gson gson = new Gson();
            String resultJson = gson.toJson(modelingResult);
            try (FileOutputStream fos = new FileOutputStream(savePath + profileId + ".json")) {
                fos.write(resultJson.getBytes(StandardCharsets.UTF_8));
                log.info("Successfully saved profile to: {} with {} records and {} clusters", 
                    savePath + profileId + ".json", resultList.size(), clusterStats.size());
            } catch (IOException e) {
                log.error("Failed to save profile file", e);
                throw new RuntimeException(e);
            }

            log.info("Academic profile model built successfully");
            return Result.success(profileId, "建模成功");

        } catch (Exception e) {
            log.error("Failed to build academic profile model", e);
            return Result.error(500, "建模失败：" + e.getMessage());
        }
    }

    /**
     * 获取学业画像结果列表
     */
    @GetMapping("/getList")
    public Result<List<Map<String, Object>>> getRecords(@RequestParam(value = "userId") String userId) {
        log.info("Fetching profile records for user: {}", userId);
        try {
            // 读取文件系统中的建模结果
            String rootPath = System.getProperty("user.dir");

            String relativePath = "middleware" + File.separator + "src" + File.separator + "main"
                    + File.separator + "java" + File.separator + "out" + File.separator + "academic" + File.separator;

            String savePath = rootPath + File.separator + relativePath;
            
            File directory = new File(savePath);


            List<Map<String, Object>> records = new ArrayList<>();
            
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));

                if (files != null) {
                    for (File file : files) {
                        Map<String, Object> record = new HashMap<>();
                        record.put("profileId", file.getName().replace(".json", ""));
                        record.put("createTime", LocalDateTime.now().toString());
                        record.put("filePath", relativePath + file.getName());
                        records.add(record);
                    }
                }
            }
            
            log.info("Found {} academic profile records for user {}", records.size(), userId);
            return Result.success(records, "success");
            
        } catch (Exception e) {
            log.error("Failed to fetch academic profile records", e);
            return Result.error(500, "获取记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取学业画像详情
     */
    @GetMapping("/getDetail")
    public Result<Map<String, Object>> getDetail(@RequestParam(value = "profileId") String profileId) {
        log.info("Fetching profile detail for ID: {}", profileId);
        
        try {
            String rootPath = System.getProperty("user.dir");
            String relativePath = "middleware" + File.separator + "src" + File.separator + "main"
                    + File.separator + "java" + File.separator + "out" + File.separator + "academic" + File.separator;
            String filePath = rootPath + File.separator + relativePath + profileId + ".json";
            
            File file = new File(filePath);
            log.info(String.valueOf(file));
            if (!file.exists()) {
                return Result.error(404, "文件不存在");
            }
            
            // 读取文件内容
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            
            // 解析完整的建模结果
            Map<String, Object> modelingResult = gson.fromJson(content, new com.google.gson.reflect.TypeToken<Map<String, Object>>() {}.getType());
            
            log.info("Successfully loaded academic profile detail with {} records", modelingResult.get("recordCount"));
            return Result.success(modelingResult, "success");
            
        } catch (Exception e) {
            log.error("Failed to fetch academic profile detail", e);
            return Result.error(500, "获取详情失败：" + e.getMessage());
        }
    }

    /**
     * 计算学生分数
     */
    @PostMapping("/calculateScores")
    public Result<PgAcademic> calculateStudentScores(@RequestBody Map<String, Object> requestData) {
        String studentId = (String) requestData.get("studentId");
        log.info("Calculating scores for student: {}", studentId);
        
        try {
            Result<PgAcademic> result = pgAcademicAnalysisService.calculateStudentScores(studentId);
            log.info("Successfully calculated scores for student: {}", studentId);
            return result;
        } catch (Exception e) {
            log.error("Failed to calculate scores for student: {}", studentId, e);
            return Result.error(500, "计算分数失败：" + e.getMessage());
        }
    }

    /**
     * 获取学生雷达图数据
     */
    @GetMapping("/radarChart")
    public Result<Map<String, Object>> getStudentRadarChartData(@RequestParam(value = "studentId") String studentId) {
        log.info("Fetching radar chart data for student: {}", studentId);
        
        try {
            Result<Map<String, Object>> result = pgAcademicAnalysisService.getStudentRadarChartData(studentId);
            log.info("Successfully fetched radar chart data for student: {}", studentId);
            return result;
        } catch (Exception e) {
            log.error("Failed to fetch radar chart data for student: {}", studentId, e);
            return Result.error(500, "获取雷达图数据失败：" + e.getMessage());
        }
    }
}
