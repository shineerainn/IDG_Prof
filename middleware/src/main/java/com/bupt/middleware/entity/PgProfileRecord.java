package com.bupt.middleware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author chenxiao
 * @date 2025/4/10 09:50
 * @description: Postgraduate Profile Record
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PgProfileRecord {
    private Long sn; // 模型序列号，自增

    private String filePath; // 文件存放路径

    private String userId; // 用户id，画像建模者

    private String profileId; // 画像编号

    private String algorithmType; // 算法类型

    private LocalDateTime createTime; // 创建时间

    private String attributeAmount; // 属性数量

    private String attributeList; // 属性列表
}
