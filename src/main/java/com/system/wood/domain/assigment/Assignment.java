package com.system.wood.domain.assigment;

import com.system.wood.domain.BaseTimeEnity;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.testcase.Testcase;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "assignment")
@NoArgsConstructor
public class Assignment extends BaseTimeEnity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long id;

    @Column(nullable = false)
    private String assignmentName;

    @Lob
    private String description;

    private String language;

    private String languageVersion;

    private String uploadUrl; // skeleton code를 업로드한 경로

    @Column(unique = true)
    private String imageName; // docker image name

    private String targetFileName;

    private Boolean existsReport;

    private LocalDateTime dueDate; // 종료일

    @OneToOne(mappedBy = "assignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Testcase testcase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Builder
    public Assignment(String assignmentName, String description, String language, String languageVersion, String uploadUrl, String imageName, String targetFileName, Boolean existsReport, LocalDateTime dueDate, Subject subject) {
        this.assignmentName = assignmentName;
        this.description = description;
        this.language = language;
        this.languageVersion = languageVersion;
        this.uploadUrl = uploadUrl;
        this.imageName = imageName;
        this.targetFileName = targetFileName;
        this.existsReport = existsReport;
        this.dueDate = dueDate;
        this.subject = subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
