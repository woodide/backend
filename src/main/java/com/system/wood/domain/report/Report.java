package com.system.wood.domain.report;

import com.system.wood.domain.BaseTimeEnity;
import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.student.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Report extends BaseTimeEnity {

    @Id
    @GeneratedValue
    @Column(name = "report_id")
    private Long id;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    public Report(String content, Student student, Assignment assignment) {
        this.content = content;
        this.student = student;
        this.assignment = assignment;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
