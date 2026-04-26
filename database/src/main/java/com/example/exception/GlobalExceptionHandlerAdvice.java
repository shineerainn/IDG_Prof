//package com.example.exception;
//
//import com.example.pojo.ResponseMessage;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice //统一处理
//public class GlobalExceptionHandlerAdvice {
//
//    Logger log = LoggerFactory.getLogger(GlobalExceptionHandlerAdvice.class);
//    // Logger log = LoggerFactory.getLogger(GlobalExceptionHandlerAdvice.class);
//
//    @ExceptionHandler({Exception.class})
//    public ResponseMessage handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) {
//        log.error("统一异常", e);
//        System.out.println(log);
//        return new ResponseMessage(401, "error", null);
//    }
//}


package com.example.exception;

import com.example.pojo.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandlerAdvice.class);

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMessage<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        // 收集所有字段错误
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);

            // 详细日志记录
            log.error("参数校验失败 - 字段: {}, 值: {}, 原因: {}",
                    fieldName,
                    error.getRejectedValue(),
                    errorMessage);
        });

        // 返回结构化错误信息
        return ResponseMessage.error(400, "参数校验失败", errors);
    }

    /**
     * 处理其他所有异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseMessage<String> handleGlobalException(
            Exception e,
            HttpServletRequest request) {

        log.error("统一异常处理 - 异常类型: {}, 请求路径: {}, 错误信息: {}",
                e.getClass().getSimpleName(),
                request.getRequestURI(),
                e.getMessage(),
                e);  // 最后一个参数会输出完整堆栈

        return ResponseMessage.error(500, "系统繁忙，请稍后再试");
    }
}