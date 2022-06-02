package com.system.wood.global.error;

import org.springframework.http.HttpStatus;

public class ContainerException extends RuntimeException{

    private HttpStatus status;

    public ContainerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }
}
