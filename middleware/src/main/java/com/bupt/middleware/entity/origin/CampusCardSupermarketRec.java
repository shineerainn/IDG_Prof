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
public class CampusCardSupermarketRec {

    private String recordId;       // 记录id，主键 (对应数据库 record_id)
    private String pgId;          // 学号，外键 (对应数据库 PG_id)
    private Date shoppingTime;    // 业务发生时间 (对应数据库 shopping_time)
    private BigDecimal amount;    // 消费金额 (DECIMAL(5,2)类型)
}