package com.system.wood.web.student.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.report.Report;
import com.system.wood.domain.student.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDto {

    private String containerName;
    private String content;

    public ReportDto(String content) {
        this.content = content;
    }

    public ReportDto(String containerName, String content) {
        this.containerName = containerName;
        this.content = content;
    }

    public Report toEntity(Student student, Assignment assignment) {
        return new Report(content, student, assignment);
    }
}
