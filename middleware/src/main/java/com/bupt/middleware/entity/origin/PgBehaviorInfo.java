package com.bupt.middleware.entity.origin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PgBehaviorInfo {
    // 基础信息
    private String pgId;                  // 学号，主键

    // 进出校行为
    private Integer ioFrequency;          // 进出校次数
    private String schoolGate;            // 主要进出校门
    private String way;                   // 主要通行凭据类型

    // 食堂消费行为
    private Integer eatingFrequency;      // 每天食堂消费次数
    private BigDecimal eatingAmount;      // 每天食堂消费金额
    private String canteen;               // 主要消费食堂
    private String paymentMethod;         // 食堂消费主要支付方式

    // 浴室消费行为
    private Date showingTime;             // 浴室消费时间
    private String showingPlace;          // 浴室消费地点

    // 超市消费行为
    private Date latestShoppingTime;      // 最近一次超市消费时间
    private BigDecimal shoppingAmount;    // 超市消费金额

    // 网络使用行为
    private Integer linkNumber;           // 校园网连接次数
    private String latestLinkPlace;       // 主要连接地点
    private Date linkTime;                // 最近一次连接时间
    private String equipment;             // 主要接入设备

    // 论坛行为
    private Date upTime;                  // 上线时间
    private Integer postNumber;           // 论坛发帖次数
    private String postModel;             // 主要发帖模块
    private Date latestPostTime;          // 最近一次发帖时间
    private Integer replyNumber;          // 回帖次数
    private String replyPostModel;        // 主要回帖模块
    private Date latestReplyTime;         // 最近一次回帖时间

    // 心理行为
    private Integer feedbackNumber;       // 异常行为反馈次数
    private String feedbackPath;          // 主要反馈路径
    private String latestSpecificProblem; // 最近一次反馈内容
    private Date recordTime;              // 反馈时间
    private String problemType;           // 问题种类

    // 学业信息
    private BigDecimal requiredCredits;   // 应修学分
    private BigDecimal completedCredits;  // 已修学分
    private BigDecimal average;           // 平均分
    private BigDecimal gpa;               // 绩点
    private BigDecimal failedCredits;     // 不及格学分
    private String paper;                 // 小论文/创新成果完成情况
    private String graduationDesign;      // 毕业设计完成情况
    private String studentStatus;         // 学业状态

    // 就业信息
    private String workingStatus;         // 工作状态
    private String workingPlace;          // 工作单位

    // 家庭背景
    private String parentsEducation;      // 父母受教育程度
    private String familyIncome;          // 家庭经济水平
    private String hometown;              // 成长环境
    private Boolean isOnlyChild;          // 是否独生子女

    // 分析标签
    private Integer classId;              // 画像类型
    private Integer classificationId;     // 预警类别
    private Integer flag;                 // 标志
}