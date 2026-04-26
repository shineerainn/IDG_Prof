package com.bupt.middleware.controller;

import com.bupt.middleware.entity.SystemConfig;
import com.bupt.middleware.entity.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenxiao
 * @date 2025/4/5 00:24
 * @description: System Config
 */

@RestController
@RequestMapping("/data")
@Slf4j
public class SysConfig {

//    public static void main(String[] args){
//        SpringApplication.run(sysConfig.class, args);
//    }

    // 获取系统配置
    @GetMapping("/getSysConfig")
    public Result<SystemConfig> getSysConfig() {
        SystemConfig systemConfig = new SystemConfig();
        // TODO: 调用数据模块接口获取系统配置，并判定是否成功
        return Result.success(systemConfig, "success");
    }

    // 修改系统配置
    @PutMapping("/updateSysConfig")
    public Result<String> updateSysConfig(@RequestBody SystemConfig systemConfig) {
        // TODO: 调用数据模块接口修改系统配置，并判定是否成功
        return Result.success(String.format("Update System Config %s!", systemConfig));
    }
}
