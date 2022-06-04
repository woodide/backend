package com.system.wood.web.user.dto;

import com.system.wood.domain.Role;
import com.system.wood.domain.professor.Professor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfSignUpDto {

    private String email;

    private String password;

    private String username;

    public Professor toEntity() {
        return Professor.builder()
                .email(email)
                .password(password)
                .username(username)
                .role(Role.PROFESSOR)
                .build();
    }
}
