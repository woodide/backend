package com.system.wood.web.professor.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class AssignmentResDto {

    private String assignmentName;
    private String description;
    private String imageName;
    private Boolean existsReport;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;

    public AssignmentResDto(String assignmentName, String description, String imageName, Boolean existsReport, LocalDateTime dueDate) {
        this.assignmentName = assignmentName;
        this.description = description;
        this.imageName = imageName;
        this.existsReport = existsReport;
        this.dueDate = dueDate;
    }
}
