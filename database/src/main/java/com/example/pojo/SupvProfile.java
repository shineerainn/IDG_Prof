package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name="supv_profile")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupvProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sn")
    private Long sn; // 模型序列号，自增

    @Column(name = "file_path")
    private String filePath; // 文件存放路径

    @Column(name = "user_id")
    private String userId; // 用户id，画像建模者

    @Column(name="profile_id")
    private String profileId; // 画像编号

    @Column(name="algorithm_type")
    private String algorithmType; // 算法类型

    @Column(name="create_time")
    private LocalDateTime createTime; // 创建时间

    @Column(name="attribute_amount")
    private String attributeAmount; // 属性数量

    @Column(name="attribute_list", length = 8191)
    private String attributeList; // 属性列表
}
