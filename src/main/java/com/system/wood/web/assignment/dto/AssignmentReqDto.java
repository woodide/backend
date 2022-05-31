package com.system.wood.web.assignment.dto;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.member.Member;
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

    public Assignment toEntity(String uploadUrl, String imageUrl, Member creator) {
        return Assignment.builder()
                .assignmentName(assignmentName)
                .description(description)
                .language(language)
                .languageVersion(languageVersion)
                .uploadUrl(uploadUrl)
                .imageUrl(imageUrl)
                .creator(creator)
                .build();
    }
}
