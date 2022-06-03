package com.system.wood.web.assignment.dto;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Lob;

@Getter
@Setter
public class AssignmentReqDto2 {

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

    private Long subjectId;

    public Assignment toEntity(String uploadUrl, User creator) {
        return Assignment.builder()
                .assignmentName(assignmentName)
                .description(description)
                .language(language)
                .languageVersion(languageVersion)
//                .testcase(testcase)
//                .expectedResult(expectedResult)
                .uploadUrl(uploadUrl)
//                .creator(creator)
                .build();
    }
}
