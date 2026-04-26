package com.bupt.middleware.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * @author chenxiao
 * @date 2025/4/12 00:17
 * @description:
 */
public class Attribute {

    // 解析attributeList字符串（示例："[graduateName, studentID, citizenID, gender]"）
    public static List<String> parseAttributeList(String attributeListStr) {
        if (attributeListStr == null || attributeListStr.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // 去除方括号和空格
        String cleaned = attributeListStr.replaceAll("[\\[\\]\\s]", "");
        if (cleaned.isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(cleaned.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
