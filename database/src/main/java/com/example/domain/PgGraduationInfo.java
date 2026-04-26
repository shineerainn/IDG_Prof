package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PgGraduationInfo {
    private String pgId;            // 学号，主键
    private String workingStatus;   // 工作状态
    private String workingPlace;    // 工作单位
}