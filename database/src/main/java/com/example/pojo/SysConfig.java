package com.example.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="sys_config")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysConfig {

    @Id
    @Column(name="config_name")
    private String configName; // 配置名称

    @Column(name = "config_content")
    private String configContent; // 配置内容
}
