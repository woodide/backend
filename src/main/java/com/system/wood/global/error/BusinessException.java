package com.system.wood.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private HttpStatus status;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }

    public BusinessException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }
}