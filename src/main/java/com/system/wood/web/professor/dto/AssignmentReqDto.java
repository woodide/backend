package com.system.wood.web.professor.dto;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.subject.Subject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Getter
@Setter
public class AssignmentReqDto {

    @Column(nullable = false)
    private String assignmentName;

    @Lob
    private String description;

    private String language;

    private String languageVersion;

    private String targetFileName;

    private MultipartFile multipartFile;

    private MultipartFile testInput;

    private MultipartFile testOutput;

    private String subjectCode;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;

    public Assignment toEntity(String uploadUrl, String imageName, Subject subject) {
        return Assignment.builder()
                .assignmentName(assignmentName)
                .description(description)
                .language(language)
                .languageVersion(languageVersion)
                .uploadUrl(uploadUrl)
                .imageName(imageName)
                .targetFileName(targetFileName)
                .dueDate(dueDate)
                .subject(subject)
                .build();
    }
}
