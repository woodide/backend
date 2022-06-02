package com.system.wood.web.assignment.dto;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Lob;

@Getter
@Setter
public class AssignmentReqDto {

    @Column(nullable = false)
    private String assignmentName;

    @Lob
    private String description;

    private String language;

    private String languageVersion;

    private MultipartFile multipartFile;

    private MultipartFile testInput;

    private MultipartFile testOutput;

    public Assignment toEntity(String uploadUrl, String imageUrl) {
        return Assignment.builder()
                .assignmentName(assignmentName)
                .description(description)
                .language(language)
                .languageVersion(languageVersion)
                .uploadUrl(uploadUrl)
                .imageUrl(imageUrl)
                .build();
    }
}
