package com.system.wood.domain.assigment;

import com.system.wood.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "assignment")
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

    private String path; // skeleton code를 업로드한 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member creator; // ROLE가 PROFESSOR인 멤버만이 출제자가 될 수 있다.
}
