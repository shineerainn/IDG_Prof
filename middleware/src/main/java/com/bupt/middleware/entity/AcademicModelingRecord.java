package com.bupt.middleware.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 学业画像建模记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicModelingRecord {
    private String modelId;        // 模型ID
    private String userId;         // 用户ID
    private String modelName;      // 模型名称
    private String algorithmType;  // 算法类型
    private String filePath;       // 文件路径
    private LocalDateTime createTime; // 创建时间
    private String status;         // 状态
}
