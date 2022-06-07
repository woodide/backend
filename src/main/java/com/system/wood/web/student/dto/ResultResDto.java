package com.system.wood.web.student.dto;

import com.system.wood.domain.result.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ResultResDto {
    private String executionResult;
    private String submitCode;
    private Double score;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

    public ResultResDto(String executionResult, String submitCode, Double score, LocalDateTime submitTime) {
        this.executionResult = executionResult;
        this.submitCode = submitCode;
        this.score = score;
        this.submitTime = submitTime;
    }

    public static ResultResDto from(Result result) {
        return new ResultResDto(result.getExecutionResult(), result.getSubmitCode(), result.getScore(), result.getCreatedTime());
    }
}
