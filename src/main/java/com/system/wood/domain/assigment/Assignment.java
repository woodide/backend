package com.system.wood.domain.assigment;

import com.system.wood.domain.member.Member;
import com.system.wood.domain.testcase.Testcase;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "assignment")
@NoArgsConstructor
public class Assignment {

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

    private String imageName; // docker image를 업로드한 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member creator; // ROLE가 PROFESSOR인 멤버만이 출제자가 될 수 있다.

    @OneToMany(mappedBy = "assignment")
    private List<Testcase> testcaseList = new ArrayList<>();

    @Builder
    public Assignment(String assignmentName, String description, String language, String languageVersion, String uploadUrl, String imageName, Member creator) {
        this.assignmentName = assignmentName;
        this.description = description;
        this.language = language;
        this.languageVersion = languageVersion;
        this.uploadUrl = uploadUrl;
        this.imageName = imageName;
        this.creator = creator;
    }
}
