package org.example.exception;

import lombok.Getter;

/**
 * 业务异常，由 service 层抛出，携带状态码和提示信息
 */
@Getter
public class ServiceException extends RuntimeException {

    private final Integer code;

    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
