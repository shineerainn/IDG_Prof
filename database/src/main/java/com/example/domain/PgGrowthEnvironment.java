package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PgGrowthEnvironment {
    private String pgId;              // 学号，主键
    private String parentsEducation;  // 父母受教育程度
    private String familyIncome;     // 家庭经济水平
    private String hometown;         // 成长环境
    private Boolean isOnlyChild;     // 是否独生子女
}