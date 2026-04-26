package com.example.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPwdDto {

    @NotBlank(message = "用户id不能为空")
    private String userId;

    @NotBlank(message = "用户密码不能为空")
    private String userPwd;
}
