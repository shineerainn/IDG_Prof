package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PgBasicInfo {

    private String pgId;           // 学号，主键 (对应数据库 PG_id)
    private String name;           // 姓名
    private String idCard;         // 身份证号 (对应数据库 ID_card)
    private String gender;         // 性别
    private String grade;          // 年级
    private String classId;        // 班级 (对应数据库 class_id)
    private String major;          // 专业
    private String hometown;       // 籍贯（XX省XX市）
    private String politicalStatus; // 政治面貌 (对应数据库 political_status)
    private String counselorId;    // 辅导员工号 (对应数据库 counselor_id)
    private String supvId;         // 导师工号 (对应数据库 Supv_id)
    private String phone;          // 联系电话
    private String photo;          // 照片（文件存储路径）
}