package com.bupt.middleware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
    private String userId;      // 用户id
    private String userRole;   // 用户权限
}