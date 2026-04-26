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
public class SupvBehaviorInfo {
    // 基础信息
    private String supvId;               // 工号，主键

    // 性格特征
    private String personalityType;     // MBTI性格分类
    private BigDecimal logicalThinking;  // 逻辑性(0-10分)
    private BigDecimal expressiveness;   // 表达性(0-10分)
    private BigDecimal communicationSkill; // 沟通性(0-10分)
    private BigDecimal influence;        // 影响力(0-10分)

    // 语言特征
    private BigDecimal speechRate;       // 语速(0-10分)
    private String speechStyle;          // 语言风格

    // 教学特征
    private BigDecimal classroomInteraction; // 课堂互动性(0-10分)
    private BigDecimal studentFocus;     // 关注学生个体(0-10分)
    private String teachingStyle;        // 教学风格
    private BigDecimal politicalContent; // 思政内容融入(0-10分)
    private BigDecimal studentFeedback;   // 学生反馈(0-10分)

    // 分析标签
    private Integer classId;            // 画像类别
}