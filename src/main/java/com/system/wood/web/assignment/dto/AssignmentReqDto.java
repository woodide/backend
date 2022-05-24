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

    @Lob
    private String testcase;

    @Lob
    private String expectedResult;

    private MultipartFile multipartFile;

    public Assignment toEntity(String uploadUrl, Member creator) {
        return Assignment.builder()
                .assignmentName(assignmentName)
                .description(description)
                .language(language)
                .languageVersion(languageVersion)
                .testcase(testcase)
                .expectedResult(expectedResult)
                .uploadUrl(uploadUrl)
                .creator(creator)
                .build();
    }
}
