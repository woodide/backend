package com.system.wood.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400
    UsernameOrPasswordNotFoundException (HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 일치하지 않습니다."),
    CANNOT_CREATE_CONTAINER(HttpStatus.BAD_REQUEST, "컨테이너를 생성할 수 없습니다."),
    CANNOT_STOP_CONTAINER(HttpStatus.BAD_REQUEST, "컨테이너를 중지할 수 없습니다."),
    CANNOT_KILL_CONTAINER(HttpStatus.BAD_REQUEST, "컨테이너를 삭제할 수 없습니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 잘못 입력되었습니다"),

    // 401
    UNAUTHORIZEDException (HttpStatus.UNAUTHORIZED, "로그인 후 이용가능합니다."),
    ExpiredJwtException(HttpStatus.UNAUTHORIZED, "기존 토큰이 만료되었습니다. 해당 토큰을 가지고 get-newtoken링크로 이동해주세요."),
    ReLogin(HttpStatus.UNAUTHORIZED, "모든 토큰이 만료되었습니다. 다시 로그인해주세요."),

    // 403
    ForbiddenException(HttpStatus.FORBIDDEN, "해당 요청에 대한 권한이 없습니다."),
    IS_NOT_OWNER(HttpStatus.FORBIDDEN, "유저는 해당 컨테이너의 소유자이 아닙니다.")
    ;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    private HttpStatus status;
    private String message;

}
