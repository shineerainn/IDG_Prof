package com.example.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="counselor")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Counselor {

    @Id
    @Column(name="work_id")
    private String workId; // 辅导员id

    @Column(name="counselor_name")
    private String counselorName; // 辅导员姓名
}
