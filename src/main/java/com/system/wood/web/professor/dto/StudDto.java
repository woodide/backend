package com.system.wood.web.professor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudDto {
    private String studentNumber;
    private String email;
    private String username;

    public StudDto(String studentNumber, String email, String username) {
        this.studentNumber = studentNumber;
        this.email = email;
        this.username = username;
    }
}

