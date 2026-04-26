package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author chenxiao
 * @date 2025/8/28
 * @description: CORS跨域配置 - 修复Session Cookie传递问题
 */
@Configuration
@EnableWebMvc
@Slf4j
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("=== 开始配置CORS跨域设置 ===");

        registry.addMapping("/**")
                // 允许的来源（根据你的实际部署环境调整）
                .allowedOriginPatterns("*")
                .allowedOrigins(
                        "http://localhost:8080",
                        "http://localhost:3000",
                        "http://localhost:3000",
                        "http://127.0.0.1:8080"
                )
                // 允许的HTTP方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                // 允许的请求头
                .allowedHeaders("*")
                // 关键：允许发送凭据（Session Cookie）
                .allowCredentials(true)
                // 预检请求缓存时间
                .maxAge(3600)
                // 允许客户端访问的响应头
                .exposedHeaders(
                        "Set-Cookie",
                        "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials"
                );

        log.info("CORS配置完成：");
        log.info("- 允许凭据传递: true");
        log.info("- 允许所有来源模式: *");
        log.info("- 支持Session Cookie传递");
        log.info("=== CORS配置完成 ===");
    }
}
