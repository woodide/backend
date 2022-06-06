package com.system.wood.infra.dto;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.result.Result;
import com.system.wood.domain.student.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradingDto {

    private String result;
    private String submitCode;
    private Double score;

    public GradingDto(String result, String submitCode, Double score) {
        this.result = result;
        this.submitCode = submitCode;
        this.score = score;
    }

    public Result toEntity(Student student, Assignment assignment) {
        return Result.builder()
                .score(score)
                .executionResult(result)
                .submitCode(submitCode)
                .student(student)
                .assignment(assignment)
                .build();
    }
}
