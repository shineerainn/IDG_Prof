package com.example;

import com.example.dao.UserPwdDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DatabaseApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private UserPwdDao user_pwdDao;

    @Test
    void getUserPwd() {
        System.out.println(user_pwdDao.GetUserPwd("2020211001"));
//        System.out.println("Hello");
    }

}
