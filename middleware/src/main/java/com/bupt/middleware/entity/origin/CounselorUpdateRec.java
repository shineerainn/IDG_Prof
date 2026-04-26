package com.bupt.middleware.entity.origin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CounselorUpdateRec {
    private String recId;          // 记录id，主键
    private String counselorId;    // 辅导员工号
    private String pgId;          // 学生学号
    private String result;         // 修改结果
    private String reason;        // 修改原因
}