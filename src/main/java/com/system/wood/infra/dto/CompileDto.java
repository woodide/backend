package com.system.wood.infra.dto;

import lombok.Getter;

@Getter
public class CompileDto {
    String compileResult;
    String exeFileName;
    Boolean compileSuccess;

    public CompileDto(String compileResult, String exeFileName, Boolean compileSuccess) {
        this.compileResult = compileResult;
        this.exeFileName = exeFileName;
        this.compileSuccess = compileSuccess;
    }
}
