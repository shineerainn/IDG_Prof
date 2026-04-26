package com.example.service;

import com.example.pojo.UserPwd;
import com.example.pojo.dto.UserPwdDto;
import com.example.repository.UserPwdRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPwdService implements IUserPwdService {

    @Autowired
    private UserPwdRepository userPwdRepository;

    @Override
    public UserPwd add(UserPwdDto userPwdDto) {
        UserPwd userPwd = new UserPwd();
        BeanUtils.copyProperties(userPwdDto, userPwd);
        return userPwdRepository.save(userPwd);
    }

    @Override
    public UserPwd getUser(String userId) {
        System.out.println("🔍 开始查询用户: " + userId);

        Optional<UserPwd> result = userPwdRepository.findById(userId);

        System.out.println("📊 是否找到用户: " + result.isPresent());

        if (result.isPresent()) {
            UserPwd user = result.get();
            System.out.println("✅ 找到用户对象: " + user);
            System.out.println("🚀 返回用户对象");
            return user;
        } else {
            System.out.println("❌ 用户不存在");
            throw new IllegalArgumentException("用户不存在，参数异常！");
        }
    }

    @Override
    public UserPwd edit(UserPwdDto userPwdDto) {
        UserPwd userPwd = new UserPwd();
        BeanUtils.copyProperties(userPwdDto, userPwd);
        return userPwdRepository.save(userPwd);
    }

    @Override
    public void delete(String userId) {
        userPwdRepository.deleteById(userId);
    }
}