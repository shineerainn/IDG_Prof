package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PsyAbnormalityRec {

    private String recordId;        // 记录id，主键
    private String pgId;           // 学号，外键
    private String feedbackPath;   // 反馈路径：心理排查，深度辅导...
    private String problemType;    // 问题种类：学业，就业，情感...
    private String specificProblem; // 具体问题（TEXT类型）
    private Date recordTime;       // 记录时间
}