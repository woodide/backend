package com.system.wood.web.professor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudResDto {

    private Boolean isSubmit;
    private String studentNumber;

    private String email;
    private String username;
    private Double bestScore;
    private Integer count;
    private String executionResult;
    private String submitCode;

    public StudResDto(String email, Boolean isSubmit, String studentNumber, String username, Double bestScore, Integer count, String executionResult, String submitCode) {
        this.email = email;
        this.isSubmit = isSubmit;
        this.studentNumber = studentNumber;
        this.username = username;
        this.bestScore = bestScore;
        this.count = count;
        this.executionResult = executionResult;
        this.submitCode = submitCode;
    }
}
