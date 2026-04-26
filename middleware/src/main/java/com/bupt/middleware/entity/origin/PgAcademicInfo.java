package com.bupt.middleware.entity.origin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PgAcademicInfo {

    private String pgId;               // 学号，主键
    private BigDecimal requiredCredits; // 应修学分
    private BigDecimal completedCredits; // 已修学分
    private BigDecimal average;        // 平均分
    private BigDecimal gpa;           // 绩点
    private BigDecimal failedCredits;  // 不及格学分
    private String paper;             // 小论文/创新成果完成情况
    private String graduationDesign;   // 毕业设计完成情况
    private String pgStatus;          // 学业状态
}