package com.system.wood.web.student.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectResDto {

    private String subjectName;
    private String code;

    public SubjectResDto(String subjectName, String code) {
        this.subjectName = subjectName;
        this.code = code;
    }
}
