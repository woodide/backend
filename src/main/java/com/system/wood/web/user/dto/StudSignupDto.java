package com.system.wood.web.user.dto;

import com.system.wood.domain.Role;
import com.system.wood.domain.student.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudSignupDto {

    private String email;

    private String password;

    private String username;

    private String studentNumber;

    public Student toEntity() {
        return Student.builder()
                .email(email)
                .password(password)
                .username(username)
                .studentNumber(studentNumber)
                .role(Role.STUDENT)
                .build();
    }
}
