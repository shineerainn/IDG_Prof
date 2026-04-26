package com.bupt.middleware.entity.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenxiao
 * @date 2025/4/5 23:38
 * @description: ResultDTO
 */
@Data
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public ResultDTO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static<T> ResultDTO<T> success(T data) {
        return new ResultDTO<T>(200, "success", data);
    }

    public static<T> ResultDTO<T> success() {
        return new ResultDTO<T>(200, "success", null);
    }

}
