package com.system.wood.web.professor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudResDto {

    private String studentNumber;
    private String email;
    private String username;

    public StudResDto(String studentNumber, String email, String username) {
        this.studentNumber = studentNumber;
        this.email = email;
        this.username = username;
    }
}
