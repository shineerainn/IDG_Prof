package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="user_pwd")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPwd {

    @Id
    @Column(name="user_id")
    private String userId; // 用户id

    @Column(name="user_pwd")
    private String userPwd; // 用户密码

    // 手动添加getter方法解决JSON序列化问题
    public String getUserId() {
        return userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}
