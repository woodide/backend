package com.system.wood.web.professor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentResDto {

    private String assignmentName;
    private String description;
    private String imageName;
//    private LocalDateTime dueDate;


    public AssignmentResDto(String assignmentName, String description, String imageName) {
        this.assignmentName = assignmentName;
        this.description = description;
        this.imageName = imageName;
    }
}
