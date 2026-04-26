package com.bupt.middleware.controller;

import com.bupt.middleware.entity.SupvModelBuildDTO;
import com.bupt.middleware.entity.SupvProfileRecord;
import com.bupt.middleware.entity.SupvWideTable;
import com.bupt.middleware.entity.SupvWideTableAttributes;
import com.bupt.middleware.entity.result.Result;
import com.bupt.middleware.service.ISupvProfileService;
import com.bupt.middleware.service.ISupvWideTableService;
import com.bupt.middleware.utils.ModelName;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static com.bupt.middleware.utils.Attribute.parseAttributeList;

/**
 * @author chenxiao
 * @date 2025/4/9 17:08
 * @description: Supervisor Profile Controller
 */

@RestController
@RequestMapping("/profile/supv")
@Slf4j
public class SupvProfileController {

    @Autowired
    private ISupvProfileService supvProfileService;

    @Autowired
    private ISupvWideTableService supvWideTableService;

    // 辅助类用于存储中间统计结果
    private static class ClusterStats {
        int count;
        Map<String, AttributeStats> attributes;
    }

    private static class AttributeStats {
        double numericSum;
        Map<Object, Integer> valueFrequency = new HashMap<>();
    }

    /**
     * 生成导师聚类画像（全过程）
     */
    @PostMapping("/build")
    public Result<String> buildModel(@RequestBody SupvModelBuildDTO supvModelBuildDTO) throws IllegalAccessException {
        log.info("Starting to build profile model for user: {}", supvModelBuildDTO.getUserId());

        String userId = supvModelBuildDTO.getUserId();
        SupvWideTableAttributes supvWideTableAttributes = supvModelBuildDTO.getSupvWideTableAttributes();

        // 获取选择的属性列表
        List<String> attList = new ArrayList<>();
        Class<? extends SupvWideTableAttributes> cls = supvWideTableAttributes.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if ((Boolean) field.get(supvWideTableAttributes)) {
                attList.add(field.getName());
            }
        }
        log.info("Selected attributes for clustering: {}", attList);

        // 获取全部导师宽表数据
        log.debug("Fetching all supervisor wide table data");
        List<SupvWideTable> supvWideTables = supvWideTableService.getAllSupv().getData();
        log.info("Fetched {} supervisor records for clustering", supvWideTables.size());

        // TODO: 获取算法配置表

        // 调用聚类build接口
        log.info("Starting clustering process with {} attributes", attList.size());
        List<Map<String, Object>> resultList = supvProfileService.buildSupvProfile(attList, supvWideTables, null);
        log.info("Clustering completed. Result contains {} records", resultList.size());

        // 确定命名 supervisor_功能类型_日期_时间
        String profileId = ModelName.getModelName("supervisor", "profile");
        log.info("Generated profile ID: {}", profileId);

        // 画像结果存文件
        String rootPath = System.getProperty("user.dir");
        String relativePath = "middleware" + File.separator + "src" + File.separator + "main"
                + File.separator + "java" + File.separator + "out" + File.separator + "supervisor" + File.separator;
        String savePath = rootPath + File.separator + relativePath;
        log.debug("Preparing to save profile to: {}", savePath);

        File file = new File(savePath);
        if (file.mkdirs()) {
            log.info("Created directory: {}", savePath);
        }

        Gson gson = new Gson();
        String resultListJson = gson.toJson(resultList);
        try (FileOutputStream fos = new FileOutputStream(savePath + profileId + ".json")) {
            fos.write(resultListJson.getBytes());
            log.info("Successfully saved profile to: {}", savePath + profileId + ".json");
        } catch (IOException e) {
            log.error("Failed to save profile file", e);
            throw new RuntimeException(e);
        }

        // 记录存数据库
        LocalDateTime localDateTime = LocalDateTime.now();
        SupvProfileRecord supvProfileRecord = new SupvProfileRecord();

        supvProfileRecord.setFilePath(relativePath + profileId + ".json");
        supvProfileRecord.setUserId(userId);
        supvProfileRecord.setProfileId(profileId);
        supvProfileRecord.setAlgorithmType("k-means");
        supvProfileRecord.setCreateTime(localDateTime);
        supvProfileRecord.setAttributeAmount(String.valueOf(attList.size()));
        supvProfileRecord.setAttributeList(attList.toString());

        log.info("Saving profile record to database: {}", supvProfileRecord);
        Result<String> saveResult = supvProfileService.postSupvProfileRecord(supvProfileRecord);

        if (saveResult.getCode() == 200) {
            log.info("Profile record saved successfully");
            return Result.success(profileId, "success");
        } else {
            log.error("Failed to save profile record. Error code: {}, message: {}",
                    saveResult.getCode(), saveResult.getMsg());
            return Result.error(saveResult.getCode(), saveResult.getMsg());
        }
    }

    /**
     * 获取画像结果列表
     * param: userId
     * return: Result<List<SupvProfileRecord>>
     */
    @GetMapping("/getList")
    public Result<List<SupvProfileRecord>> getRecords(@RequestParam(value = "userId") String userId) {
        log.info("Fetching profile records for user: {}", userId);
        Result<List<SupvProfileRecord>> result = supvProfileService.getSupvProfileRecords(userId);
        log.info("Found {} profile records for user {}",
                result.getData() != null ? result.getData().size() : 0, userId);
        return result;
    }

    /**
     * 根据profileId获取单条画像结果
     * param: profileId
     * return: Result<SupvProfileRecord>
     */
    @GetMapping("/getRecord")
    public Result<SupvProfileRecord> getRecord(@RequestParam(value = "profileId") String profileId) {
        log.info("Fetching profile record with ID: {}", profileId);
        Result<SupvProfileRecord> result = supvProfileService.getSupvProfileRecord(profileId);
        if (result.getCode() != 200) {
            log.warn("Failed to fetch profile record {}. Error: {}", profileId, result.getMsg());
        } else {
            log.debug("Successfully fetched profile record: {}", result.getData());
        }
        return result;
    }

    /**
     * 查询簇分布情况（按照数据库中属性顺序返回均值（非数值型为众数））
     * param: profileId
     * return: Result<List<Map<String, Object>>>
     */
    @GetMapping("/getClusterDistribution")
    public Result<List<Map<String, Object>>> getClusterDistribution(@RequestParam(value = "profileId") String profileId) {
        log.info("Processing cluster distribution for profile: {}", profileId);
        try {
            // 1. 获取记录信息
            log.debug("Fetching profile record from database");
            Result<SupvProfileRecord> recordResult = supvProfileService.getSupvProfileRecord(profileId);
            if (recordResult.getCode() != 200) {
                log.error("Failed to fetch profile record. Error code: {}", recordResult.getCode());
                return Result.error(recordResult.getCode(), recordResult.getMsg());
            }

            SupvProfileRecord record = recordResult.getData();
            String resultFilePath = record.getFilePath();
            String rootPath = System.getProperty("user.dir");
            String filePath = Paths.get(rootPath, resultFilePath.split("[\\\\/]+")).toString();
            log.debug("Profile file path resolved to: {}", filePath);

            // 2. 解析attributeList获取属性顺序
            List<String> orderedAttributes = parseAttributeList(record.getAttributeList());
            if (orderedAttributes.isEmpty()) {
                log.warn("No valid attributes found in attributeList for profile {}", profileId);
                return Result.error(400, "No valid attributes found in attributeList");
            }
            log.debug("Ordered attributes: {}", orderedAttributes);

            // 3. 读取JSON文件
            List<Map<String, Object>> dataList;
            try (FileReader reader = new FileReader(filePath)) {
                Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                dataList = new Gson().fromJson(reader, listType);
                log.debug("Successfully read {} records from profile file",
                        dataList != null ? dataList.size() : 0);
            } catch (IOException e) {
                log.error("Failed to read profile file: {}", filePath, e);
                return Result.error(500, "Failed to read profile file");
            }

            if (dataList == null || dataList.isEmpty()) {
                log.warn("No data found in profile file: {}", filePath);
                return Result.success(Collections.emptyList(), "No valid attributes found in attributeList");
            }

            // 4. 预提取属性和初始化数据结构
            Map<Double, ClusterStats> clusterStatsMap = new HashMap<>();
            log.debug("Processing {} records for cluster statistics", dataList.size());

            // 5. 单次遍历处理所有数据
            for (Map<String, Object> item : dataList) {
                Object clusterResult = item.get("clusterResult");
                if (clusterResult == null) {
                    log.debug("Skipping record with null clusterResult");
                    continue; // 跳过clusterResult为null的记录
                }

                double clusterId;
                try {
                    clusterId = ((Number) clusterResult).doubleValue();
                } catch (ClassCastException e) {
                    log.warn("Invalid clusterResult type: {}", clusterResult.getClass());
                    continue;
                }

                ClusterStats stats = clusterStatsMap.computeIfAbsent(clusterId, k -> {
                    ClusterStats s = new ClusterStats();
                    s.count = 0;
                    s.attributes = new HashMap<>();
                    return s;
                });
                stats.count++;

                // 只处理attributeList中指定的属性
                for (String attr : orderedAttributes) {
                    Object value = item.get(attr);
                    if (value == null) {
                        log.trace("Null value for attribute {} in cluster {}", attr, clusterId);
                        continue;
                    }

                    AttributeStats attrStats = stats.attributes.computeIfAbsent(attr, k -> new AttributeStats());

                    if (value instanceof Number) {
                        attrStats.numericSum += ((Number) value).doubleValue();
                    } else {
                        attrStats.valueFrequency.merge(value, 1, Integer::sum);
                    }
                }
            }

            // 6. 构建最终结果（按照attributeList顺序）
            List<Map<String, Object>> result = new ArrayList<>();
            log.debug("Building final result for {} clusters", clusterStatsMap.size());

            for (Map.Entry<Double, ClusterStats> entry : clusterStatsMap.entrySet()) {
                Double clusterId = entry.getKey();
                if (clusterId == null) {
                    log.debug("Skipping null cluster ID");
                    continue; // 跳过key为null的条目
                }

                Map<String, Object> clusterInfo = new LinkedHashMap<>();
                clusterInfo.put("id", clusterId.toString());
                clusterInfo.put("value", entry.getValue().count);

                List<Object> attributeValues = new ArrayList<>();
                ClusterStats stats = entry.getValue();

                for (String attr : orderedAttributes) {
                    AttributeStats attrStats = stats.attributes.get(attr);
                    if (attrStats == null) {
                        log.trace("No stats for attribute {} in cluster {}", attr, clusterId);
                        attributeValues.add(null);
                        continue;
                    }

                    if (attrStats.numericSum != 0) {
                        // 数值型：计算均值
                        double avg = attrStats.numericSum / stats.count;
                        attributeValues.add(String.format("%.2f", avg));
                    } else {
                        // 非数值型：找出频率最高的值
                        Object mode = attrStats.valueFrequency.entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse(null);
                        attributeValues.add(mode);
                    }
                }

                clusterInfo.put("attributes", attributeValues);
                result.add(clusterInfo);
            }

            log.info("Successfully processed cluster distribution with {} clusters", result.size());
            return Result.success(result, "success");
        } catch (Exception e) {
            log.error("Error processing cluster distribution for profile {}", profileId, e);
            return Result.error(500, "Internal server error");
        }
    }

    /**
     * 按照profileId获取attributeList，从而获取list中属性对应的全部样本均值
     * param: profileId
     * return: Result<List<Map<String, Object>>>
     */
    @GetMapping("/getAllAttrMean")
    public Result<List<Map<String, Object>>> getAllAttrMean(@RequestParam(value = "profileId") String profileId) {
        log.info("Calculating attribute means for profile: {}", profileId);
        try {
            // 1. 获取记录信息
            log.debug("Fetching profile record from database");
            Result<SupvProfileRecord> recordResult = supvProfileService.getSupvProfileRecord(profileId);
            if (recordResult.getCode() != 200) {
                log.error("Failed to fetch profile record. Error code: {}", recordResult.getCode());
                return Result.error(recordResult.getCode(), recordResult.getMsg());
            }

            SupvProfileRecord record = recordResult.getData();
            String resultFilePath = record.getFilePath();
            String rootPath = System.getProperty("user.dir");
            String filePath = Paths.get(rootPath, resultFilePath.split("[\\\\/]+")).toString();
            log.debug("Profile file path resolved to: {}", filePath);

            // 2. 解析attributeList获取属性顺序
            List<String> orderedAttributes = parseAttributeList(record.getAttributeList());
            if (orderedAttributes.isEmpty()) {
                log.warn("No valid attributes found in attributeList for profile {}", profileId);
                return Result.error(400, "No valid attributes found in attributeList");
            }
            log.debug("Ordered attributes: {}", orderedAttributes);

            // 3. 读取JSON文件
            List<Map<String, Object>> dataList;
            try (FileReader reader = new FileReader(filePath)) {
                Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                dataList = new Gson().fromJson(reader, listType);
                log.debug("Successfully read {} records from profile file",
                        dataList != null ? dataList.size() : 0);
            } catch (IOException e) {
                log.error("Failed to read profile file: {}", filePath, e);
                return Result.error(500, "Failed to read profile file");
            }

            if (dataList == null || dataList.isEmpty()) {
                log.warn("No data found in profile file: {}", filePath);
                return Result.success(Collections.emptyList(), "No data found in profile file");
            }

            // 4. 初始化统计数据结构
            Map<String, AttributeStats> attributeStatsMap = new HashMap<>();
            int totalCount = 0;
            log.debug("Processing {} records for attribute means", dataList.size());

            // 5. 遍历所有数据并统计
            for (Map<String, Object> item : dataList) {
                totalCount++;

                for (String attr : orderedAttributes) {
                    Object value = item.get(attr);
                    if (value == null) {
                        log.trace("Null value for attribute {}", attr);
                        continue;
                    }

                    AttributeStats stats = attributeStatsMap.computeIfAbsent(attr, k -> new AttributeStats());

                    if (value instanceof Number) {
                        stats.numericSum += ((Number) value).doubleValue();
                    } else {
                        stats.valueFrequency.merge(value, 1, Integer::sum);
                    }
                }
            }

            // 6. 构建结果
            List<Object> resultValues = new ArrayList<>();
            log.debug("Calculating means for {} attributes", orderedAttributes.size());

            for (String attr : orderedAttributes) {
                AttributeStats stats = attributeStatsMap.get(attr);
                if (stats == null) {
                    log.trace("No stats found for attribute {}", attr);
                    resultValues.add(null);
                    continue;
                }

                if (stats.numericSum != 0) {
                    // 数值型：计算均值
                    double avg = stats.numericSum / totalCount;
                    resultValues.add(avg);
                } else {
                    // 非数值型：找出频率最高的值
                    Object mode = stats.valueFrequency.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse(null);
                    resultValues.add(mode);
                }
            }

            // 7. 按照要求的格式返回结果
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("attributes", resultValues);
            log.info("Successfully calculated attribute means for {} attributes", resultValues.size());

            return Result.success(Collections.singletonList(resultMap), "success");
        } catch (Exception e) {
            log.error("Error processing all attributes mean for profile {}", profileId, e);
            return Result.error(500, "Internal server error");
        }
    }

    /**
     * 获取某属性的分箱结果
     * param: profileId, clusterId, attributeId, binningNum
     * return: Result<List<Map<String, Object>>>
     */
    @GetMapping("/getAttrBinning")
    public Result<List<Map<String, Object>>> getAttrBinning(@RequestParam(value = "profileId") String profileId,
                                                            @RequestParam(value = "clusterId") String clusterId,
                                                            @RequestParam(value = "attributeId") String attributeId,
                                                            @RequestParam(value = "binningNum") Integer binningNum) {
        log.info("Starting binning processing - profileId: {}, clusterId: {}, attribute: {}, bins: {}",
                profileId, clusterId, attributeId, binningNum);

        try {
            // 1. 获取文件路径
            log.debug("Fetching profile record...");
            Result<SupvProfileRecord> recordResult = supvProfileService.getSupvProfileRecord(profileId);
            if (recordResult.getCode() != 200) {
                log.warn("Failed to fetch profile record. Error code: {}", recordResult.getCode());
                return Result.error(recordResult.getCode(), recordResult.getMsg());
            }

            SupvProfileRecord record = recordResult.getData();
            String filePath = Paths.get(System.getProperty("user.dir"), record.getFilePath()).toString();
            log.debug("Resolved file path: {}", filePath);

            // 2. 读取JSON数据
            log.debug("Reading JSON data...");
            List<Map<String, Object>> dataList = readJsonData(filePath);
            if (dataList == null || dataList.isEmpty()) {
                log.warn("No data found or empty dataset");
                return Result.success(Collections.emptyList(), "No data found or empty dataset");
            }
            log.info("Successfully read {} records", dataList.size());

            // 3. 过滤指定cluster的数据
            log.debug("Filtering data by cluster...");
            List<Map<String, Object>> clusterData = filterByCluster(dataList, clusterId);
            if (clusterData.isEmpty()) {
                log.warn("No data found for clusterId: {}", clusterId);
                return Result.success(Collections.emptyList(), "No data found for clusterId");
            }
            log.info("Filtered dataset contains {} records", clusterData.size());

            // 4. 检查属性是否存在
            if (!clusterData.getFirst().containsKey(attributeId)) {
                log.warn("Attribute {} not found in dataset", attributeId);
                return Result.error(400, "Attribute not found");
            }

            // 5. 根据数据类型进行分箱统计
            Object sampleValue = clusterData.getFirst().get(attributeId);
            List<Map<String, Object>> result;

            if (sampleValue instanceof Number) {
                log.info("Numeric attribute detected [{}], performing binning...", attributeId);
                result = handleNumericBinning(clusterData, attributeId, binningNum);
            } else {
                log.info("Discrete attribute detected [{}], performing frequency count...", attributeId);
                result = handleDiscreteBinning(clusterData, attributeId);
            }

            log.info("Binning completed. Generated {} result items", result.size());
            return Result.success(result, "success");
        } catch (NumberFormatException e) {
            log.error("Invalid clusterId format: {}", clusterId, e);
            return Result.error(400, "Invalid clusterId format");
        } catch (IOException e) {
            log.error("Failed to read JSON file", e);
            return Result.error(500, "Data file read error");
        } catch (Exception e) {
            log.error("Unexpected error during binning processing", e);
            return Result.error(500, "Binning processing error");
        }
    }

    /**
     * 获取画像模型某簇与全体的对比数据
     * param: profileId, clusterId
     * return: Result<List<Map<String, Object>>>
     */
    @GetMapping("/getAttrComparison")
    public Result<List<Map<String, Object>>> getAttrComparison(@RequestParam(value = "profileId") String profileId,
                                                               @RequestParam(value = "clusterId") String clusterId) {
        log.info("Starting attribute comparison - profileId: {}, clusterId: {}", profileId, clusterId);

        try {
            // 1. 获取记录信息
            Result<SupvProfileRecord> recordResult = supvProfileService.getSupvProfileRecord(profileId);
            if (recordResult.getCode() != 200) {
                log.error("Failed to fetch profile record. Error code: {}", recordResult.getCode());
                return Result.error(recordResult.getCode(), recordResult.getMsg());
            }

            SupvProfileRecord record = recordResult.getData();
            String filePath = Paths.get(System.getProperty("user.dir"), record.getFilePath()).toString();
            log.debug("Resolved file path: {}", filePath);

            // 2. 获取模型选中的属性列表
            List<String> selectedAttributes = parseAttributeList(record.getAttributeList());
            if (selectedAttributes.isEmpty()) {
                log.warn("No selected attributes found for profile {}", profileId);
                return Result.error(400, "No selected attributes found for profile " + profileId);
            }
            log.debug("Selected attributes: {}", selectedAttributes);

            // 3. 读取JSON数据
            List<Map<String, Object>> allData = readJsonData(filePath);
            if (allData == null || allData.isEmpty()) {
                log.warn("No data found in profile file");
                return Result.success(Collections.emptyList(), "No data found in profile file");
            }
            log.info("Loaded {} records from profile", allData.size());

            // 4. 过滤指定cluster的数据
            double targetCluster = Double.parseDouble(clusterId);
            List<Map<String, Object>> clusterData = filterByCluster(allData, clusterId);
            if (clusterData.isEmpty()) {
                log.warn("No data found for clusterId: {}", clusterId);
                return Result.success(Collections.emptyList(), "No data found for clusterId");
            }
            log.info("Found {} records in cluster {}", clusterData.size(), clusterId);

            // 5. 获取所有数值型属性
            Set<String> numericAttributes = new HashSet<>();
            for (String attr : selectedAttributes) {
                Object value = allData.getFirst().get(attr);
                if (value instanceof Number && !attr.equals("clusterResult")) {
                    numericAttributes.add(attr);
                }
            }
            log.debug("Found {} numeric attributes in selected: {}", numericAttributes.size(), numericAttributes);

            if (numericAttributes.isEmpty()) {
                log.warn("No numeric attributes found in selected attributes");
                return Result.success(Collections.emptyList(), "No numeric attributes found in selected attributes");
            }

            // 6. 计算全局统计信息
            Map<String, Double> globalStats = calculateGlobalStats(allData, numericAttributes);

            // 7. 计算簇统计信息
            Map<String, Double> clusterStats = calculateClusterStats(clusterData, numericAttributes);

            // 8. 构建结果
            Map<String, Object> result = new HashMap<>();
            result.put("clusterId", clusterId);
            result.put("value", clusterData.size());

            List<Map<String, Object>> attributesComparison = new ArrayList<>();

            for (String attr : numericAttributes) {
                double clusterMean = clusterStats.get(attr);
                double globalMean = globalStats.get(attr);
                double allMin = globalStats.get(attr + "_min");
                double allMax = globalStats.get(attr + "_max");

                // 计算归一化值
//                double clusterNorm = (clusterMean - allMin) / (allMax - allMin);
//                double globalNorm = (globalMean - allMin) / (allMax - allMin);
                double clusterNorm = calculateLogNormalization(clusterMean, allMin, allMax);
                double globalNorm = calculateLogNormalization(globalMean, allMin, allMax);

                Map<String, Object> attrComparison = new HashMap<>();
                attrComparison.put("attrName", attr);
                attrComparison.put("rowMean", Arrays.asList(String.format("%.2f", clusterMean), String.format("%.2f", globalMean)));
                attrComparison.put("normValue", Arrays.asList(String.format("%.2f", clusterNorm), String.format("%.2f", globalNorm)));

                attributesComparison.add(attrComparison);
            }

            result.put("attributes", attributesComparison);
            log.info("Successfully generated comparison for {} numeric attributes", numericAttributes.size());

            return Result.success(Collections.singletonList(result), "success");
        } catch (NumberFormatException e) {
            log.error("Invalid clusterId format: {}", clusterId, e);
            return Result.error(400, "Invalid clusterId format");
        } catch (IOException e) {
            log.error("Failed to read JSON file", e);
            return Result.error(500, "Data file read error");
        } catch (Exception e) {
            log.error("Unexpected error during comparison processing", e);
            return Result.error(500, "Comparison processing error");
        }
    }

    // 辅助方法：计算全局统计信息（均值、最小、最大）
    private Map<String, Double> calculateGlobalStats(List<Map<String, Object>> data, Set<String> numericAttributes) {
        Map<String, Double> sumMap = new HashMap<>();
        Map<String, Double> minMap = new HashMap<>();
        Map<String, Double> maxMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        // 初始化
        for (String attr : numericAttributes) {
            sumMap.put(attr, 0.0);
            minMap.put(attr, Double.MAX_VALUE);
            maxMap.put(attr, -Double.MAX_VALUE);
            countMap.put(attr, 0);
        }

        // 遍历所有数据
        for (Map<String, Object> item : data) {
            for (String attr : numericAttributes) {
                Object value = item.get(attr);
                if (value == null) continue;

                double numValue = ((Number) value).doubleValue();
                sumMap.put(attr, sumMap.get(attr) + numValue);
                countMap.put(attr, countMap.get(attr) + 1);

                if (numValue < minMap.get(attr)) {
                    minMap.put(attr, numValue);
                }
                if (numValue > maxMap.get(attr)) {
                    maxMap.put(attr, numValue);
                }
            }
        }

        // 计算均值并合并结果
        Map<String, Double> result = new HashMap<>();
        for (String attr : numericAttributes) {
            int count = countMap.get(attr);
            if (count > 0) {
                result.put(attr, sumMap.get(attr) / count);
                result.put(attr + "_min", minMap.get(attr));
                result.put(attr + "_max", maxMap.get(attr));
            }
        }

        return result;
    }

    // 辅助方法：计算簇统计信息（均值）
    private Map<String, Double> calculateClusterStats(List<Map<String, Object>> data, Set<String> numericAttributes) {
        Map<String, Double> sumMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        // 初始化
        for (String attr : numericAttributes) {
            sumMap.put(attr, 0.0);
            countMap.put(attr, 0);
        }

        // 遍历簇数据
        for (Map<String, Object> item : data) {
            for (String attr : numericAttributes) {
                Object value = item.get(attr);
                if (value == null) continue;

                double numValue = ((Number) value).doubleValue();
                sumMap.put(attr, sumMap.get(attr) + numValue);
                countMap.put(attr, countMap.get(attr) + 1);
            }
        }

        // 计算均值
        Map<String, Double> result = new HashMap<>();
        for (String attr : numericAttributes) {
            int count = countMap.get(attr);
            if (count > 0) {
                result.put(attr, sumMap.get(attr) / count);
            }
        }

        return result;
    }

    // 辅助方法：对数变换归一化（适合右偏分布）
    private double calculateLogNormalization(double value, double min, double max) {
        try {
            // 1. 处理异常值
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                return 0.5; // 默认中值
            }

            // 2. 处理所有值相同的情况
            if (min == max) {
                return 0.5;
            }

            // 3. 确保最小值>=0（对于有负值的属性需要特殊处理）
            double offset = 0;
            if (min <= 0) {
                offset = -min + 1; // 保证平移后最小值至少为1
            }

            // 4. 对数变换（使用log1p避免精度损失）
            double transformedValue = Math.log1p(value + offset);
            double transformedMin = Math.log1p(min + offset);
            double transformedMax = Math.log1p(max + offset);

            // 5. 归一化并限制在[0,1]范围内
            double normalized = (transformedValue - transformedMin) / (transformedMax - transformedMin);
            return Math.min(1.0, Math.max(0.0, normalized));
        } catch (Exception e) {
            log.warn("Log normalization error for value: {}, min: {}, max: {}. Error: {}",
                    value, min, max, e.getMessage());
            return 0.5; // 出错时返回中值
        }
    }

    // 辅助方法：读取JSON数据
    private List<Map<String, Object>> readJsonData(String filePath) throws IOException {
        log.debug("Reading JSON file: {}", filePath);
        try (FileReader reader = new FileReader(filePath)) {
            Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
            return new Gson().fromJson(reader, type);
        }
    }

    // 辅助方法：按cluster过滤数据
    private List<Map<String, Object>> filterByCluster(List<Map<String, Object>> dataList, String clusterId) {
        List<Map<String, Object>> result = new ArrayList<>();
        double targetCluster = Double.parseDouble(clusterId);
        log.debug("Filtering for clusterId: {}", targetCluster);

        for (Map<String, Object> item : dataList) {
            Object clusterResult = item.get("clusterResult");
            if (clusterResult != null && ((Number) clusterResult).doubleValue() == targetCluster) {
                result.add(item);
            }
        }
        return result;
    }

    // 辅助方法：处理数值型分箱
    private List<Map<String, Object>> handleNumericBinning(
            List<Map<String, Object>> data, String attributeId, int binningNum) {

        // 获取最小最大值
        log.debug("Processing numeric binning for attribute: {}, bins: {}", attributeId, binningNum);
        double[] minMax = getMinMax(data, attributeId);
        log.debug("Attribute {} - min: {}, max: {}", attributeId, minMax[0], minMax[1]);

        double min = minMax[0], max = minMax[1];
        double binWidth = (max - min) / binningNum;
        log.debug("Calculated bin width: {}", binWidth);

        // 初始化分箱
        int[] counts = new int[binningNum];
        for (Map<String, Object> item : data) {
            Object value = item.get(attributeId);
            if (value == null) continue;

            double numValue = ((Number) value).doubleValue();
            int binIndex = Math.min((int) ((numValue - min) / binWidth), binningNum - 1);
            counts[binIndex]++;
        }

        // 构建结果
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < binningNum; i++) {
            double lower = min + i * binWidth;
            double upper = (i == binningNum - 1) ? max : min + (i + 1) * binWidth;

            Map<String, Object> bin = new HashMap<>();
            bin.put("range", String.format("%.2f-%.2f", lower, upper));
            bin.put("count", counts[i]);
            result.add(bin);

            log.trace("Bin {}: {}-{}, count: {}", i, lower, upper, counts[i]);
        }

        log.debug("Numeric binning completed");
        return result;
    }

    // 辅助方法：获取数值属性的最小最大值
    private double[] getMinMax(List<Map<String, Object>> data, String attributeId) {
        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;
        int nullCount = 0;

        for (Map<String, Object> item : data) {
            Object value = item.get(attributeId);
            if (value == null) {
                nullCount++;
                continue;
            }

            double numValue = ((Number) value).doubleValue();
            if (numValue < min) min = numValue;
            if (numValue > max) max = numValue;
        }

        if (nullCount > 0) {
            log.debug("Ignored {} null values for attribute {}", nullCount, attributeId);
        }
        return new double[]{min, max};
    }

    // 辅助方法：处理离散值分箱
    private List<Map<String, Object>> handleDiscreteBinning(
            List<Map<String, Object>> data, String attributeId) {

        log.debug("Processing discrete value binning for attribute: {}", attributeId);
        Map<Object, Integer> counts = new HashMap<>();
        int nullCount = 0;

        for (Map<String, Object> item : data) {
            Object value = item.get(attributeId);
            if (value == null) {
                nullCount++;
                continue;
            }

            // 布尔值转为字符串
            Object key = (value instanceof Boolean) ? value.toString() : value;
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }

        if (nullCount > 0) {
            log.debug("Ignored {} null values for attribute {}", nullCount, attributeId);
        }

        // 转为结果列表并按计数排序
        List<Map<String, Object>> result = new ArrayList<>();
        counts.entrySet().stream()
                .sorted(Map.Entry.<Object, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    Map<String, Object> bin = new HashMap<>();
                    bin.put("value", entry.getKey());
                    bin.put("count", entry.getValue());
                    result.add(bin);

                    log.trace("Discrete value: {}, count: {}", entry.getKey(), entry.getValue());
                });

        log.debug("Discrete binning completed. Found {} distinct values", counts.size());
        return result;
    }
}