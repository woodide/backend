package com.system.wood.web.container.dto;

import lombok.Getter;

@Getter
public class ResponseDto {

    private ResponseDto(ReturnStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    private ReturnStatus status;
    private String message;
    private static final ResponseDto successDto = new ResponseDto(ReturnStatus.SUCCESS, "");

    public static ResponseDto of(ReturnStatus returnStatus, String message) {
        return new ResponseDto(returnStatus, message);
    }

    public static ResponseDto getSuccessDto() {
        return successDto;
    }

}
