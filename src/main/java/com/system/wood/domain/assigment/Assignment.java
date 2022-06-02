package com.system.wood.domain.assigment;

import com.system.wood.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "assignment")
@NoArgsConstructor
public class Assignment {

    @Id @GeneratedValue
    @Column(name = "assignment_id")
    private Long id;

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

    private String uploadUrl; // skeleton code를 업로드한 경로

    @Builder
    public Assignment(String assignmentName, String description, String language, String languageVersion, String testcase, String expectedResult, String uploadUrl, User creator) {
        this.assignmentName = assignmentName;
        this.description = description;
        this.language = language;
        this.languageVersion = languageVersion;
        this.testcase = testcase;
        this.expectedResult = expectedResult;
        this.uploadUrl = uploadUrl;
    }
}
