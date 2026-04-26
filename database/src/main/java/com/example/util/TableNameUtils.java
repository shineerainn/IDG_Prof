package com.example.util;

public class TableNameUtils {

    /**
     * 根据年份和学期生成表名后缀
     * @param year 年份（如2025）
     * @param semester 学期（1-上半年，2-下半年）
     * @return 表名后缀（如2025_1）
     */
    public static String generateTableSuffix(int year, int semester) {
        return year + "_" + semester; // 直接使用年份和学期，用下划线连接
    }

    /**
     * 生成完整表名
     * @param baseTableName 基础表名
     * @param year 年份
     * @param semester 学期
     * @return 完整表名
     */
    public static String generateTableName(String baseTableName, int year, int semester) {
        return baseTableName + "_" + generateTableSuffix(year, semester);
    }
}
