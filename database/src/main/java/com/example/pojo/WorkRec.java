package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name="work_rec")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkRec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rec_id")
    private Long rec_id; // 记录id，自增

    @Column(name = "user_id")
    private String userId; // 用户id，记录者

    @Column(name="student_id")
    private String studentId; // 记录的学生

    @Column(name="rec_time")
    private LocalDateTime recTime; // 创建时间

    @Column(name="rec_content")
    private String recContent; // 记录内容

    @Column(name="student_status")
    private LocalDateTime studentStatus; // 学生状态评价
}
