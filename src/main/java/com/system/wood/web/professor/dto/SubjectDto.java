package com.system.wood.web.professor.dto;

import com.system.wood.domain.subject.Subject;
import lombok.Getter;

import java.util.List;

@Getter
public class SubjectDto {
    private Long subjectId;
    private String name;
    private String code;
    private List<Long> studentsId;
    private String assignmentId;

    public Subject toEntity() {
        return new Subject(name, code);
    }
}
