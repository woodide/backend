package com.system.wood.infra.dto;

import lombok.Getter;

@Getter
public class ExecutionDto {

    String resultFileName;
    Boolean isError;

    public ExecutionDto(String resultFileName, Boolean isError) {
        this.resultFileName = resultFileName;
        this.isError = isError;
    }
}
