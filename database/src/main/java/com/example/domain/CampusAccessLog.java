package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampusAccessLog {

    private String recordId;       // 记录id，主键 (对应数据库 record_id)
    private String pgId;          // 学号，外键 (对应数据库 PG_id)
    private String type;          // 业务类型：出校，入校
    private Date time;            // 业务发生时间 (DATETIME类型)
    private String schoolGate;    // 校门编号：东门，西门，南门，北门 (对应数据库 school_gate)
    private String way;           // 通行凭据类型：刷脸，刷卡，手工登记
}