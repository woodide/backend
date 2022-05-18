package com.system.wood.web.container.dto;

import lombok.Getter;

@Getter
public class ContainerResDto {

    private ContainerResDto(ReturnStatus status) {
        this.status = status;
    }

    private ReturnStatus status;
    private static final ContainerResDto success = new ContainerResDto(ReturnStatus.SUCCESS);
    private static final ContainerResDto fail = new ContainerResDto(ReturnStatus.FAIL);

    public static ContainerResDto getSuccess() {
        return success;
    }

    public static ContainerResDto getFail() {
        return fail;
    }
}
