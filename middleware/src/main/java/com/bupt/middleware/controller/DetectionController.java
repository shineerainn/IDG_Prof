package com.bupt.middleware.controller;

import com.bupt.middleware.entity.PgModelBuildDTO;
import com.bupt.middleware.entity.PgDetectionRecord;
import com.bupt.middleware.entity.PgWideTable;
import com.bupt.middleware.entity.PgWideTableAttributes;
import com.bupt.middleware.entity.result.Result;
import com.bupt.middleware.service.IDetectionService;
import com.bupt.middleware.service.IPgWideTableService;
import com.bupt.middleware.utils.ModelName;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author chenxiao
 * @date 2025/4/9 17:20
 * @description: Postgraduate Detection Controller
 */

@RestController
@RequestMapping("/detection")
@Slf4j
public class DetectionController {

    @Autowired
    private IDetectionService detectionService;

    @Autowired
    private IPgWideTableService pgWideTableService;

    /**
     * 构建预警模型（包含训练及预测）
     */
    @PostMapping("/build")
    public Result<String> buildModel(@RequestBody PgModelBuildDTO pgDetectionBuildDTO) throws IllegalAccessException {
        log.info("Starting model build process for user: {}", pgDetectionBuildDTO.getUserId());

        String userId = pgDetectionBuildDTO.getUserId();
        PgWideTableAttributes pgWideTableAttributes = pgDetectionBuildDTO.getPgWideTableAttributes();

        // 获取选择的属性列表
        List<String> attList = new ArrayList<>();
        Class<? extends PgWideTableAttributes> cls = pgWideTableAttributes.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if ((Boolean) field.get(pgWideTableAttributes)) {
                attList.add(field.getName());
            }
        }
        log.info("Selected attributes for detection model: {}", attList);

        // 获取全部学生宽表数据
        log.debug("Fetching all postgraduate wide table data");
        List<PgWideTable> pgWideTables = pgWideTableService.getAllPg().getData();
        log.info("Fetched {} postgraduate records for model building", pgWideTables.size());

        // TODO: 获取算法配置表

        // 调用build接口
        log.info("Starting model training with {} attributes", attList.size());
        String modelName = detectionService.buildDetectionModel(attList, pgWideTables, null);
        log.info("Model training completed. Model name: {}", modelName);

        // 预测预警结果
        log.info("Starting model inference on {} records", pgWideTables.size());
        List<Map<String, Object>> resultList = detectionService.inferDetectionModel(pgWideTables, modelName);
        log.info("Model inference completed. Generated {} prediction results", resultList.size());

        // 确定命名 graduate_功能类型_日期_时间
        // 命名规范：graduate_detection_20241009_123044
        String detectionId = ModelName.getModelName("postgraduate", "detection");
        log.info("Generated detection ID: {}", detectionId);

        // 预测结果存文件
        String rootPath = System.getProperty("user.dir");
        String relativePath = "middleware" + File.separator + "src" + File.separator + "main"
                + File.separator + "java" + File.separator + "out" + File.separator + "postgraduate" + File.separator;
        String savePath = rootPath + File.separator + relativePath;
        log.debug("Saving results to path: {}", savePath);

        File file = new File(savePath);
        if (file.mkdirs()) {
            log.info("Created directory: {}", savePath);
        }

        try (FileOutputStream fos = new FileOutputStream(savePath + detectionId + ".json")) {
            String resultListJson = new Gson().toJson(resultList);
            fos.write(resultListJson.getBytes());
            log.info("Successfully saved detection results to: {}", savePath + detectionId + ".json");
        } catch (IOException e) {
            log.error("Failed to save detection results file", e);
            throw new RuntimeException(e);
        }

        // 记录存数据库
        LocalDateTime localDateTime = LocalDateTime.now();
        PgDetectionRecord pgDetectionRecord = new PgDetectionRecord();

        pgDetectionRecord.setFilePath(relativePath + detectionId + ".json");
        pgDetectionRecord.setUserId(userId);
        pgDetectionRecord.setDetctId(detectionId);
        pgDetectionRecord.setAlgorithmType("id3Tree");
        pgDetectionRecord.setCreateTime(localDateTime);
        pgDetectionRecord.setAttributeAmount(String.valueOf(attList.size()));
        pgDetectionRecord.setAttributeList(attList.toString());

        log.info("Saving detection record to database");
        Result<String> saveResult = detectionService.postDetectionRecord(pgDetectionRecord);

        if (saveResult.getCode() == 200) {
            log.info("Detection record saved successfully");
            return Result.success(detectionId, "success");
        } else {
            log.error("Failed to save detection record. Error code: {}, message: {}",
                    saveResult.getCode(), saveResult.getMsg());
            return Result.error(saveResult.getCode(), saveResult.getMsg());
        }
    }

    /**
     * 获取预测结果列表
     * param: userId
     * return: Result<List<PgDetectionRecord>>
     */
    @GetMapping("/getList")
    public Result<List<PgDetectionRecord>> getRecords(@RequestParam(value = "userId") String userId) {
        log.info("Fetching detection records for user: {}", userId);
        Result<List<PgDetectionRecord>> result = detectionService.getDetectionRecords(userId);
        log.info("Found {} detection records for user {}",
                result.getData() != null ? result.getData().size() : 0, userId);
        return result;
    }

    /**
     * 查询预测数据分布
     * param: detectionId, listSize
     * return: Result<List<Int>>（分箱列表：每一项代表该区段的人数）
     */
    @GetMapping("/getDistribution")
    public Result<List<Integer>> getDistribution(@RequestParam(value = "detectionId") String detectionId,
                                                 @RequestParam(value = "listSize") int listSize) {
        log.info("Calculating distribution for detection {}, listSize: {}", detectionId, listSize);

        // 创建统计数组
        int[] resultList = new int[listSize];

        // 根据detectionId查询数据库，得到存储路径
        String resultFilePath = detectionService.getDetectionRecord(detectionId).getData().getFilePath();
        String rootPath = System.getProperty("user.dir");
        resultFilePath = Paths.get(rootPath, resultFilePath.split("[\\\\/]+")).toString();
        log.debug("Resolved file path: {}", resultFilePath);

        // 检查文件是否存在
        if (!new File(resultFilePath).exists()) {
            log.error("Result file not found: {}", resultFilePath);
            return Result.error(404, "No such file or directory");
        }

        // 读取结果文件
        try {
            log.debug("Reading result file");
            File file = new File(resultFilePath);
            String jsonString = FileUtils.readFileToString(file, "UTF-8");

            // 将JSON的String 转成一个JsonArray对象
            JsonArray jArray = JsonParser.parseString(jsonString).getAsJsonArray();
            log.debug("Processing {} records for distribution", jArray.size());

            // 循环遍历
            for (JsonElement jsonElement : jArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                // 按照listSize统计数据
                int index = (int) Math.floor(jsonObject.get("confidence").getAsFloat() * (float) listSize);
                if (index >= resultList.length) { index = resultList.length - 1; }
                resultList[index]++;
            }
        } catch (IOException e) {
            log.error("Error reading result file", e);
            return Result.error(404, "No such file or directory");
        }

        // 创建返回List
        List<Integer> returnList = Arrays.stream(resultList).boxed().toList();
        log.info("Distribution calculation completed. Result: {}", returnList);
        return Result.success(returnList, "success");
    }
    /**
     * 查询某分数段学生预警信息
     * param: detectionId, listSize, index
     * return: Result<List<Map<String, Object>>>
     */
    @GetMapping("/getInfoList")
    public Result<List<Map<String, Object>>> getInfoList(@RequestParam(value = "detectionId") String detectionId,
                                                         @RequestParam(value = "listSize") int listSize,
                                                         @RequestParam(value = "index") int index) {
        log.info("Fetching info list for detection {}, listSize {}, index {}",
                detectionId, listSize, index);

        // 创建返回List
        List<Map<String, Object>> resultList = new ArrayList<>();

        // 根据detectionId查询数据库，得到存储路径
        String resultFilePath = detectionService.getDetectionRecord(detectionId).getData().getFilePath();
        String rootPath = System.getProperty("user.dir");
        resultFilePath = Paths.get(rootPath, resultFilePath.split("[\\\\/]+")).toString();
        log.debug("Resolved file path: {}", resultFilePath);

        // 检查文件是否存在
        if (!new File(resultFilePath).exists()) {
            log.error("Result file not found: {}", resultFilePath);
            return Result.error(404, "No such file or directory");
        }

        // 读取结果文件
        try {
            log.debug("Reading result file");
            File file = new File(resultFilePath);
            String jsonString = FileUtils.readFileToString(file, "UTF-8");
            log.info("Get data from file: {}", resultFilePath);

            // 将JSON的String 转成一个JsonArray对象
            JsonArray jArray = JsonParser.parseString(jsonString).getAsJsonArray();
            log.debug("Processing {} records for info list", jArray.size());

            Gson gson = new Gson();

            // 循环遍历
            for (JsonElement jsonElement : jArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                // 按照listSize, index筛选数据
                int infoIndex = (int) Math.floor(jsonObject.get("confidence").getAsFloat() * (float) listSize);
                if (infoIndex == index) {
                    Map<String, Object> map = gson.fromJson(jsonObject, new TypeToken<Map<String, Object>>() {}.getType());
                    resultList.add(map);
                } else if (index == listSize - 1 && infoIndex == listSize) {
                    // confidence=1.0的情况
                    Map<String, Object> map = gson.fromJson(jsonObject, new TypeToken<Map<String, Object>>() {}.getType());
                    resultList.add(map);
                }
            }
        } catch (IOException e) {
            log.error("Error reading result file", e);
            return Result.error(404, "No such file or directory");
        }

        log.info("Found {} matching records", resultList.size());
        return Result.success(resultList, "success. Find " + resultList.size() + " records");
    }

    /**
     * 获取学生预警信息详情
     */
    @GetMapping("/getInfo")
    public Result<List<Map<String, Object>>> getInfo(@RequestParam(value = "detectionId") String detectionId,
                                                     @RequestParam(value = "studentId") String studentId) {
        log.info("Fetching detailed info for student {} in detection {}", studentId, detectionId);

        // 根据detectionId查询数据库，得到存储路径
        String resultFilePath = detectionService.getDetectionRecord(detectionId).getData().getFilePath();
        String rootPath = System.getProperty("user.dir");
        resultFilePath = Paths.get(rootPath, resultFilePath.split("[\\\\/]+")).toString();
        log.debug("Resolved file path: {}", resultFilePath);

        // 检查文件是否存在
        if (!new File(resultFilePath).exists()) {
            log.error("Result file not found: {}", resultFilePath);
            return Result.error(404, "No such file or directory");
        }

        // 读取结果文件
        try {
            log.debug("Reading result file");
            String jsonString = FileUtils.readFileToString(new File(resultFilePath), "UTF-8");
            log.info("Get data from file: {}", resultFilePath);

            // 将JSON的String 转成一个JsonArray对象
            JsonArray studentsArray = JsonParser.parseString(jsonString).getAsJsonArray();
            log.debug("Processing {} student records", studentsArray.size());

            // 数据结构初始化
            JsonObject targetStudent = null;
            Map<String, Double> numericSums = new HashMap<>();
            Map<String, Integer> numericCounts = new HashMap<>();
            Map<String, Map<String, Integer>> categoricalFrequencies = new HashMap<>();
            Map<String, int[]> booleanFrequencies = new HashMap<>(); // [falseCount, trueCount]
            Set<String> allAttributes = new HashSet<>();

            // 单次遍历收集所有统计信息
            for (JsonElement element : studentsArray) {
                JsonObject student = element.getAsJsonObject();

                // 检查学生状态是否为"在读"
                JsonElement statusElement = student.get("academicStatus");
                if (statusElement != null && statusElement.isJsonPrimitive()) {
                    String status = statusElement.getAsString();
                    if (!"在读".equals(status)) {
                        continue; // 跳过非在读学生
                    }
                }
                // 检查是否是目标学生
                if (student.get("studentId").getAsString().equals(studentId)) {
                    targetStudent = student;
                }

                // 收集所有属性
                for (Map.Entry<String, JsonElement> entry : student.entrySet()) {
                    String attr = entry.getKey();
                    if ("studentId".equals(attr)) continue;

                    allAttributes.add(attr);
                    JsonElement value = entry.getValue();

                    if (value.isJsonPrimitive()) {
                        JsonPrimitive primitive = value.getAsJsonPrimitive();

                        if (primitive.isBoolean()) {
                            // 布尔型属性统计
                            boolean boolValue = primitive.getAsBoolean();
                            int[] counts = booleanFrequencies.computeIfAbsent(attr, k -> new int[2]);
                            counts[boolValue ? 1 : 0]++;
                        } else if (primitive.isNumber()) {
                            // 数值型属性统计
                            double numValue = primitive.getAsDouble();
                            if ("postgraduateScore".equals(attr) && numValue == -1) {
                                continue; // 跳过-1的分数
                            }
                            numericSums.merge(attr, numValue, Double::sum);
                            numericCounts.merge(attr, 1, Integer::sum);
                        } else if (primitive.isString()) {
                            // 分类型属性统计
                            String strValue = primitive.getAsString();
                            categoricalFrequencies
                                    .computeIfAbsent(attr, k -> new HashMap<>())
                                    .merge(strValue, 1, Integer::sum);
                        }
                    }
                }
            }

            if (targetStudent == null
//                    || (targetStudent.has("academicStatus") &&
//                    !"在读".equals(targetStudent.get("academicStatus").getAsString()))

            ) {
                log.warn("Student {} not found in detection results", studentId);
                return Result.error(404, "Student not found in detection results");
            }

            // 构建结果
            List<Map<String, Object>> result = new ArrayList<>();
            log.debug("Building comparison data for {} attributes", allAttributes.size());

            for (String attr : allAttributes) {
                Map<String, Object> attributeInfo = new LinkedHashMap<>();
                attributeInfo.put("attribute", attr);

                JsonElement value = targetStudent.get(attr);
                if (value == null) continue;

                if (value.isJsonPrimitive()) {
                    JsonPrimitive primitive = value.getAsJsonPrimitive();

                    if (primitive.isBoolean()) {
                        // 处理布尔型属性 - 三元组(学生值, 最常见值, 该值占比)
                        boolean studentValue = primitive.getAsBoolean();
                        int[] counts = booleanFrequencies.getOrDefault(attr, new int[2]);
                        int total = counts[0] + counts[1];
                        boolean mostCommon = counts[1] > counts[0]; // true是否比false多
                        double ratio = total > 0 ? (studentValue ? counts[1]/(double)total : counts[0]/(double)total) : 0;
                        attributeInfo.put("value", Arrays.asList(studentValue, mostCommon, Math.round(ratio * 10000) / 100.0));
                        attributeInfo.put("type", "boolean");
                    } else if (primitive.isNumber()) {
                        // 处理数值型属性 - 三元组(学生值, 1delta区间, 是否在2delta区间内)
                        double studentValue = primitive.getAsDouble();
                        // 对于postgraduateScore = -1的情况，仍然显示但统计时不包含
                        boolean isValid = !("postgraduateScore".equals(attr) && studentValue == -1);

                        if (numericCounts.containsKey(attr)) {
                            double average = numericSums.get(attr) / numericCounts.get(attr);
                            // 计算标准差 - 需要重新遍历有效数据
                            double sumOfSquares = 0;
                            int validCount = 0;

                            for (JsonElement element : studentsArray) {
                                JsonObject student = element.getAsJsonObject();

                                // 检查学生状态
                                JsonElement status = student.get("academicStatus");
                                if (status != null && !"在读".equals(status.getAsString())) {
                                    continue;
                                }

                                JsonElement val = student.get(attr);
                                if (val != null && val.isJsonPrimitive() && val.getAsJsonPrimitive().isNumber()) {
                                    double v = val.getAsDouble();
                                    if (!("postgraduateScore".equals(attr) && v == -1)) {
                                        sumOfSquares += Math.pow(v - average, 2);
                                        validCount++;
                                    }
                                }
                            }
                            double stdDev = validCount > 0 ? Math.sqrt(sumOfSquares / validCount) : 0;

                            // 1 delta区间 [avg-delta, avg+delta]
                            List<Double> deltaRange = Arrays.asList(
                                    Math.round((average - stdDev) * 100) / 100.0,
                                    Math.round((average + stdDev) * 100) / 100.0
                            );

                            // 是否在2 delta 区间内
                            boolean within2Delta = isValid &&
                                    studentValue >= (average - 2 * stdDev) &&
                                    studentValue <= (average + 2 * stdDev);

                            attributeInfo.put("value", Arrays.asList(
                                    isValid ? studentValue : "N/A",
                                    deltaRange,
                                    within2Delta
                            ));
                        } else {
                            attributeInfo.put("value", Arrays.asList(
                                    isValid ? studentValue : "N/A",
                                    Arrays.asList(0.0, 0.0),
                                    false
                            ));
                        }
                        attributeInfo.put("type", "numeric");
                    } else if (primitive.isString()) {
                        // 处理分类型属性 - 三元组(学生值, 最常见值, 该值占比)
                        String studentValue = primitive.getAsString();
                        Map<String, Integer> freqMap = categoricalFrequencies.getOrDefault(attr, Collections.emptyMap());
                        int total = freqMap.values().stream().mapToInt(Integer::intValue).sum();

                        Map.Entry<String, Integer> mostFrequentEntry = freqMap.entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .orElse(null);

                        String mostFrequent = mostFrequentEntry != null ? mostFrequentEntry.getKey() : "";
                        double ratio = total > 0 ? freqMap.getOrDefault(studentValue, 0) / (double)total : 0;

                        attributeInfo.put("value", Arrays.asList(
                                studentValue,
                                mostFrequent,
                                Math.round(ratio * 10000) / 100.0 // 保留2位小数百分比
                        ));
                        attributeInfo.put("type", "categorical");
                    }

                    result.add(attributeInfo);
                }
            }

            log.info("Successfully retrieved comparison data for student {}", studentId);
            return Result.success(result, "Comparison data retrieved successfully");

        } catch (IOException e) {
            log.error("Error reading result file", e);
            return Result.error(500, "Error reading detection results file");
        } catch (Exception e) {
            log.error("Error processing detection results", e);
            return Result.error(500, "Error processing detection results");
        }
    }
}