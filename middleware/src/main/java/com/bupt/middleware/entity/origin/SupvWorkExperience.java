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
public class SupvWorkExperience {
    private String workId;          // 经历id，主键
    private String supvId;          // 工号，外键
    private String workingPlace;    // 工作单位名称
    private String jobPosition;     // 工作职位名称
    private Date startTime;         // 工作起始时间
    private Date endTime;          // 工作结束时间（为空表示在职）
}