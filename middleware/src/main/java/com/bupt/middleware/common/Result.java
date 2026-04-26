package com.bupt.middleware.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    /**
     * 成功响应
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（无数据）
     */
    public static Result<String> success(String message) {
        Result<String> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /**
     * 错误响应
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /**
     * 错误响应（默认500）
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    // 手动添加getter/setter方法
    public Integer getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }

    public void setCode(Integer code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }
}