package com.bupt.middleware.controller;

import com.bupt.middleware.entity.AlgorithmConfig;
import com.bupt.middleware.entity.result.Result;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenxiao
 * @date 2025/4/5 00:34
 * @description: Algorithm Config
 */

@RestController
@RequestMapping("/data")
public class AlgoConfig {

    // 获取算法库配置
    @GetMapping("/getAlgoConfig")
    public Result<AlgorithmConfig> getAlgoConfig() {
        AlgorithmConfig algorithmConfig = new AlgorithmConfig();
        // TODO: 调用数据模块接口获取算法库配置，，并判定是否成功
        return Result.success(algorithmConfig, "success");
    }

    // 修改算法库配置
    @PutMapping("/updateAlgoConfig")
    public Result<String> updateAlgoConfig(@RequestBody AlgorithmConfig algorithmConfig) {
        // TODO: 调用数据模块接口修改算法库配置，并判定是否成功
        return Result.success(String.format("Update Algorithm Config %s!", algorithmConfig));
    }
}
