package com.system.wood.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    CANNOT_CREATE_CONTAINER(400, "컨테이너를 생성할 수 없습니다.");

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private int status;
    private String message;
}
