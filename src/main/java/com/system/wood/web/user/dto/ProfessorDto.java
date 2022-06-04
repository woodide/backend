package com.system.wood.web.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessorDto {

    private String email;
    private String username;

    public ProfessorDto(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
