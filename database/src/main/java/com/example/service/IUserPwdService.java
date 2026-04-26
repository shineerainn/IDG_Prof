package com.example.service;

import com.example.pojo.UserPwd;
import com.example.pojo.dto.UserPwdDto;
import org.springframework.stereotype.Service;

@Service
public interface IUserPwdService {

    UserPwd add(UserPwdDto dto);

    UserPwd getUser(String userId);

    UserPwd edit(UserPwdDto dto);

    void delete(String userId);
}
