package com.bupt.middleware.entity.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
*   @author chenxiao
*   @date 2025/4/1 21:20
*   @description: 中控统一返回结果
*/
@Data
@NoArgsConstructor
public class Result<T> implements Serializable {

    private Integer code;   // 状态码
    private String msg; // 信息
    private Timestamp timestamp;
    private T data; // 数据

    // 成功，无数据返回
    public static <T> Result<T> success(String msg) {
        Result<T> result = new Result<T>();
        result.code = 200;
        result.msg = msg;
        result.timestamp = new Timestamp(System.currentTimeMillis());
        return result;
    }

    // 成功，有数据返回
    public static <T> Result<T> success(T data, String msg) {
        Result<T> result = new Result<T>();
        result.code = 200;
        result.msg = msg;
        result.timestamp = new Timestamp(System.currentTimeMillis());
        result.data = data;
        return result;
    }

    // 失败
    public static <T> Result<T> error(Integer errCode, String msg) {
        Result<T> result = new Result<T>();
        result.code = errCode;
        result.msg = msg;
        result.timestamp = new Timestamp(System.currentTimeMillis());
        return result;
    }
}
