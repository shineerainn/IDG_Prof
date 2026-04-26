package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupvInfo {
    private String supvId;          // 工号，主键
    private String name;            // 姓名
    private String gender;          // 性别
    private String degree;          // 学位
    private String graduateSchool;  // 毕业院校
    private String job;             // 职务
    private String title;           // 职称
    private String type;            // 类别：导师，辅导员
    private String email;           // 电子邮箱
    private String photo;           // 照片路径
}