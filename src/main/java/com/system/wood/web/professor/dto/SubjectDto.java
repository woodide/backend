package com.system.wood.web.professor.dto;

import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.subject.Subject;
import lombok.Getter;

@Getter
public class SubjectDto {
    private String name;
    private String code;

    public Subject toEntity(Professor professor) {
        return new Subject(name, code, professor);
    }

    public SubjectDto(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
