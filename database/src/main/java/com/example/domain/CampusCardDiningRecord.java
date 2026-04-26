package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampusCardDiningRecord {

    private String recordId;        // 记录id，主键 (对应数据库 record_id)
    private String pgId;           // 学号，外键 (对应数据库 PG_id)
    private String meal;           // 业务类型：早餐，午餐，晚餐，夜宵
    private Date eatingTime;       // 业务发生时间 (对应数据库 eating_time)
    private String canteen;        // 业务发生地点
    private BigDecimal amount;     // 消费金额 (DECIMAL(5,2)类型)
    private String paymentMethod;  // 支付方式：刷卡，微信支付，支付宝支付 (对应数据库 payment_method)
}