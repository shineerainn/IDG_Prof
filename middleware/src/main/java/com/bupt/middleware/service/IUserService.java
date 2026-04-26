package com.bupt.middleware.service;

import com.bupt.middleware.entity.UserPwd;
import com.bupt.middleware.entity.UserRole;
import com.bupt.middleware.entity.result.Result;
import org.springframework.stereotype.Service;

/**
 * @author chenxiao
 * @date 2025/4/8 18:12
 * @description: Interface of User Service
 */

@Service
public interface IUserService {
    /**
     * 获取用户密码
     */
    Result<UserPwd> getPwd(String userId);

    /**
     * 修改用户密码
     */
    Result<String> updatePwd(UserPwd newUserPwd);

    /**
     * 增加用户
     */
    Result<String> addUser(UserRole userRole);

    /**
     * 获取用户权限
     */
    Result<UserRole> getRole(String userId);

    /**
     * 修改用户权限
     */
    Result<String> updateRole(UserRole newUserRole);
}
