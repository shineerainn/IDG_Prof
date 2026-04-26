package com.bupt.middleware.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author chenxiao
 * @date 2025/4/10 09:32
 * @description: Create Model Name
 */
public class ModelName {

    public static String getModelName(String userType, String recordType) {
        // 确定命名 graduate_功能类型_日期_时间
        // 命名规范：graduate_detection_20241009_103025
        String detectionId = userType + "_" + recordType;
        LocalDateTime localDateTime = LocalDateTime.now();
        String timestamp = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        detectionId = detectionId + "_" + timestamp;


        return detectionId;
    }
}
