package com.example.pojo;

import lombok.Data;

import java.util.Map;

@Data
public class ResponseMessage<T> {
    private Integer code;
    private String message;
    private T data;

    public ResponseMessage(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应（带数据）
    public static <T> ResponseMessage<T> success(T data) {
        return new ResponseMessage<>(200, "success", data);
    }

    // 成功响应（无数据）
    public static <T> ResponseMessage<T> success() {
        return new ResponseMessage<>(200, "success", null);
    }

    // 错误响应（带错误信息）
    public static <T> ResponseMessage<T> error(int code, String message) {
        return new ResponseMessage<>(code, message, null);
    }

    // 错误响应（带详细错误数据）
    public static <T> ResponseMessage<T> error(int code, String message, T errorData) {
        return new ResponseMessage<>(code, message, errorData);
    }

    public void error(String s, String studentAcademicPerformance, String message, Exception e) {
    }
}
