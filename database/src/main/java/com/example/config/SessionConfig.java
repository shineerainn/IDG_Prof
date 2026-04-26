package com.example.config;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenxiao
 * @date 2025/8/28
 * @description: Session配置 - 增强Session管理和调试
 */
@Configuration
@Slf4j
public class SessionConfig {

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {

            @Override
            public void sessionCreated(HttpSessionEvent se) {
                log.info("=== Session创建事件 ===");
                log.info("Session ID: {}", se.getSession().getId());
                log.info("创建时间: {}", new java.util.Date(se.getSession().getCreationTime()));
                log.info("最大不活动间隔: {} 秒", se.getSession().getMaxInactiveInterval());
                log.info("是否新创建: {}", se.getSession().isNew());

                // 设置Session的默认属性
                se.getSession().setMaxInactiveInterval(24 * 60 * 60); // 24小时

                log.info("Session创建完成");
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                log.info("=== Session销毁事件 ===");
                log.info("Session ID: {}", se.getSession().getId());

                // 记录Session中保存的用户信息
                try {
                    String userId = (String) se.getSession().getAttribute("userId");
                    if (userId != null) {
                        log.info("销毁用户Session: {}", userId);
                    }
                } catch (Exception e) {
                    log.warn("获取Session中的用户信息时出错: {}", e.getMessage());
                }

                log.info("Session销毁完成");
            }
        };
    }
}