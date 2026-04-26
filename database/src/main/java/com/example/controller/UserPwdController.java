package com.example.controller;

import com.example.pojo.ResponseMessage;
import com.example.pojo.UserPwd;
import com.example.pojo.dto.UserPwdDto;
import com.example.service.IUserPwdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/database/userPwd")
public class UserPwdController {

    @Autowired
    private IUserPwdService userPwdService;

    //增加
    @PostMapping
    public ResponseEntity<ResponseMessage<UserPwd>> add(@Validated @RequestBody UserPwdDto userPwdDto) {
        UserPwd userPwd = userPwdService.add(userPwdDto);
        ResponseMessage<UserPwd> response = ResponseMessage.success(userPwd);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //查询
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseMessage<UserPwd>> get(@PathVariable String userId) {
        UserPwd userPwd = userPwdService.getUser(userId);
        ResponseMessage<UserPwd> response = ResponseMessage.success(userPwd);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //修改
    @PutMapping
    public ResponseEntity<ResponseMessage<UserPwd>> edit(@Validated @RequestBody UserPwdDto userPwdDto) {
        UserPwd userPwd = userPwdService.edit(userPwdDto);
        ResponseMessage<UserPwd> response = ResponseMessage.success(userPwd);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    //删除
    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseMessage<UserPwd>> delete(@PathVariable String userId) {
        userPwdService.delete(userId);
        ResponseMessage<UserPwd> response = ResponseMessage.success();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}