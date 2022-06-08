package com.system.wood.infra.dto;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.result.Result;
import com.system.wood.domain.student.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDto {

    private String executionResult;
    private String submitCode;
    private Double score;

    public ResultDto(String executionResult, String submitCode, Double score) {
        this.executionResult = executionResult;
        this.submitCode = submitCode;
        this.score = score;
    }

    public Result toEntity(Student student, Assignment assignment, Container container) {
        return Result.builder()
                .score(score)
                .executionResult(executionResult)
                .submitCode(submitCode)
                .student(student)
                .assignment(assignment)
                .build();
    }
}
