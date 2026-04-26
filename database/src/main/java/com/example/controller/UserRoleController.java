package com.example.controller;

import com.example.pojo.ResponseMessage;
import com.example.pojo.UserRole;
import com.example.pojo.dto.UserRoleDto;
import com.example.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/database/userRole")
public class UserRoleController {

    @Autowired
    private IUserRoleService userRoleService;

    @PostMapping
    public ResponseMessage<UserRole> add(@Validated @RequestBody UserRoleDto userRoleDto) {
        UserRole userRole = userRoleService.add(userRoleDto);
        return ResponseMessage.success(userRole);
    }

    @GetMapping("/{userId}")
    public ResponseMessage<UserRole> get(@PathVariable String userId) {
        UserRole userRole = userRoleService.getUser(userId);
        return ResponseMessage.success(userRole);
    }

    @PutMapping
    public ResponseMessage<UserRole> edit(@Validated @RequestBody UserRoleDto userRoleDto) {
        UserRole userRole = userRoleService.edit(userRoleDto);
        return ResponseMessage.success(userRole);
    }

    @DeleteMapping("/{userId}")
    public ResponseMessage<UserRole> delete(@PathVariable String userId) {
        userRoleService.delete(userId);
        return ResponseMessage.success();
    }
}
