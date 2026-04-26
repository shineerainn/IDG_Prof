package com.bupt.middleware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author chenxiao
 * @date 2025/4/9 22:02
 * @description: Postgraduate Detection Record List
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PgDetectionRecord {

    private Long sn; // 模型序列号，自增

    private String filePath; // 文件存放路径

    private String userId; // 用户id，预警建模者

    private String detctId; // 预警编号

    private String algorithmType; // 算法类型

    private LocalDateTime createTime; // 创建时间

    private String attributeAmount; // 属性数量

    private String attributeList; // 属性列表
}
