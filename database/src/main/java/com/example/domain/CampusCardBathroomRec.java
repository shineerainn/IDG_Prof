package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampusCardBathroomRec {

    private String recordId;        // 记录id，主键 (对应数据库 record_id)
    private String pgId;           // 学号，外键 (对应数据库 PG_id)
    private Date showeringTime;    // 业务发生时间 (对应数据库 showing_time)
    private String showeringPlace; // 业务发生地点 (对应数据库 showing_place)
    private BigDecimal amount;     // 消费金额 (DECIMAL(5,2)类型)
}