package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.ServiceException;
import org.example.pojo.Result;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，将 service 层的业务异常包装为统一响应格式
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public Result handleServiceException(ServiceException e) {
        log.warn("业务异常：{}", e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError == null ? "请求参数不合法" : fieldError.getDefaultMessage();
        log.warn("请求参数校验失败：{}", message);
        return Result.fail(400, message);
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("服务器内部异常", e);
        return Result.fail(500, "服务器内部错误");
    }
}
