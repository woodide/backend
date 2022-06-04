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

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;


    public AssignmentResDto(String assignmentName, String description, String imageName, LocalDateTime dueDate) {
        this.assignmentName = assignmentName;
        this.description = description;
        this.imageName = imageName;
        this.dueDate = dueDate;
    }
}
