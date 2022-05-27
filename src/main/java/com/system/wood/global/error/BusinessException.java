package com.system.wood.global.error;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private int status;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }

    public BusinessException(String msg, int status) {
        super(msg);
        this.status = status;
    }
}