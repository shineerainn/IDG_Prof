package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysConfig {
    private String sysName;               // 系统名称
    private String sysVersion;           // 版本号
    private String developerName;        // 开发者
    private String developerEmail;       // 开发者邮箱
    private String collectTime;          // 自动采集数据定时
    private String dataCourse;           // 自动采集数据源
    private String updateWideTableTime;  // 更新宽表定时
    private String updateProfileTime;    // 更新数字画像定时
    private String updateDeteTime;       // 更新预警定时
}