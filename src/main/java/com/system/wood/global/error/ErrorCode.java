package com.system.wood.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    CANNOT_CREATE_CONTAINER(400, "컨테이너를 생성할 수 없습니다."),
    CANNOT_STOP_CONTAINER(400, "컨테이너를 중지할 수 없습니다."),
    CANNOT_KILL_CONTAINER(400, "컨테이너를 삭제할 수 없습니다."),
    IS_NOT_OWNER(403, "유저는 해당 컨테이너의 소유자이 아닙니다.")
    ;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private int status;
    private String message;
}
