package com.system.wood.web.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResDto {

    private String token;
    private Boolean isProfessor;

    public LoginResDto(String token, Boolean isProfessor) {
        this.token = token;
        this.isProfessor = isProfessor;
    }
}
