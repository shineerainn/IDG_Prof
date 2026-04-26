package com.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO<T> {

    private Integer code;
    private String message;
    private T data;

    /**
     * 成功响应
     */
    public static <T> ResultDTO<T> success(T data) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（无数据）
     */
    public static ResultDTO<String> success(String message) {
        ResultDTO<String> result = new ResultDTO<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /**
     * 错误响应
     */
    public static <T> ResultDTO<T> error(Integer code, String message) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /**
     * 错误响应（默认500）
     */
    public static <T> ResultDTO<T> error(String message) {
        return error(500, message);
    }

    // 手动添加getter/setter方法（避免Lombok问题）
    public Integer getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }

    public void setCode(Integer code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }
}
