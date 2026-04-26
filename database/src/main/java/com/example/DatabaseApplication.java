package com.example;

import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication //(exclude = {DataSourceAutoConfiguration.class})
//@MapperScan
public class DatabaseApplication {

    public static void main(String[] args) {
        System.out.println("Hello");
        SpringApplication.run(DatabaseApplication.class, args);
        System.out.println("Hello World");
    }

}
