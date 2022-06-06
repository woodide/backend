package com.system.wood.infra.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradingDto {

    private String result;
    private String submitCode;
    private Double grade;

    public GradingDto(String result, String submitCode, Double grade) {
        this.result = result;
        this.submitCode = submitCode;
        this.grade = grade;
    }
}
