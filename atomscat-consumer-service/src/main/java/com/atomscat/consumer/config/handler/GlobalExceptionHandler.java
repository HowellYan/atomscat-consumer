package com.atomscat.consumer.config.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {


    @ExceptionHandler()
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Object> exceptionHandle(Exception e) {
        // 处理方法参数的异常类型
        Map<String, Object> map = new HashMap<>();
        map.put("code", "000000");
        map.put("message", e.getMessage());
        return map;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Object> handle(Exception e) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "000000");
        map.put("message", e.getCause().getMessage());
        return map;
    }

}