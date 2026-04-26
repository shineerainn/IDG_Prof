package com.bupt.middleware.entity.origin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CounselorWorkRec {
    private String recId;            // 记录id，主键
    private String counselorId;     // 辅导员工号
    private String pgId;           // 学生学号
    private String problemType;    // 问题种类
    private String specificProblem; // 具体问题
    private String workRecord;     // 工作记录
    private Date recordTime;       // 记录时间
}