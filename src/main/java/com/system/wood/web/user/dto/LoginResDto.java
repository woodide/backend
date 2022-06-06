package com.system.wood.web.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResDto {

    private String token;
    private String username;
    private String email;
    private Boolean isProfessor;

    public LoginResDto(String token,String username, String email, Boolean isProfessor) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.isProfessor = isProfessor;
    }
}
