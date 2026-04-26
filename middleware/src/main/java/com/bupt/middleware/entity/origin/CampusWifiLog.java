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
public class CampusWifiLog {

    private String recordId;       // 记录id，主键 (对应数据库 record_id)
    private String pgId;          // 学号，外键 (对应数据库 PG_id)
    private String type;          // 业务类型：登入，登出
    private Date networkTime;     // 业务发生时间 (对应数据库 network_time)
    private String networkPlace;  // 业务发生地点 (对应数据库 network_place)
    private String equipment;     // 接入设备
}