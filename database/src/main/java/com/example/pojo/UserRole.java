package com.example.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="user_role")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @Id
    @Column(name="user_id")
    private String userId; // 用户id

    @Column(name="user_role")
    private String userRole; // 用户权限
}
