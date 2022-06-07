package com.system.wood.web.container.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseContainerDto {


    public ResponseContainerDto(Integer portNum, String containerName, String assignmentName, LocalDateTime dueDate,String description) {
        this.portNum = portNum;
        this.containerName = containerName;
        this.assignmentName = assignmentName;
        this.dueDate = dueDate;
        this.description = description;
    }

    private Integer portNum;

    private String containerName;

    private String assignmentName;

    private LocalDateTime dueDate;


    private String description;

}
