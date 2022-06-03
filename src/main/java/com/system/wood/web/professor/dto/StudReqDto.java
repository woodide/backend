package com.system.wood.web.professor.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StudReqDto {

    private String subjectCode;
    private List<String> StudentNumberList = new ArrayList<>();
}
