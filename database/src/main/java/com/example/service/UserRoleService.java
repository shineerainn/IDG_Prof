package com.example.service;

import com.example.pojo.UserRole;
import com.example.pojo.dto.UserRoleDto;
import com.example.repository.UserRoleRespository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService implements IUserRoleService{

    @Autowired
    private UserRoleRespository userRoleRespository;

    @Override
    public UserRole add(UserRoleDto userRoleDto) {

        UserRole userRole = new UserRole();

        BeanUtils.copyProperties(userRoleDto, userRole);

        return userRoleRespository.save(userRole);
    }

    @Override
    public UserRole getUser(String userId) {
        return userRoleRespository.findById(userId).orElseThrow(() -> {
            throw new IllegalArgumentException("用户不存在，参数异常！");
        });
    }

    @Override
    public UserRole edit(UserRoleDto userRoleDto) {
        UserRole userRole = new UserRole();

        BeanUtils.copyProperties(userRoleDto, userRole);

        return userRoleRespository.save(userRole);
    }

    @Override
    public void delete(String userId) {
        userRoleRespository.deleteById(userId);
    }
}
