package com.example.service;

import com.example.pojo.UserRole;
import com.example.pojo.dto.UserRoleDto;
import org.springframework.stereotype.Service;

@Service
public interface IUserRoleService {

    UserRole add(UserRoleDto userRoleDto);

    UserRole getUser(String userId);

    UserRole edit(UserRoleDto userRoleDto);

    void delete(String userId);
}
