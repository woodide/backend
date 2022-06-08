package com.system.wood.domain.result;

import com.system.wood.domain.BaseTimeEnity;
import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.student.Student;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Result extends BaseTimeEnity {

    @Id @GeneratedValue
    @Column(name = "result_id")
    private Long id;

    private Double score;

    @Lob
    private String submitCode;

    @Lob
    private String executionResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "container_id")
    private Container container;

    @Builder
    public Result(Double score, String submitCode, String executionResult, Student student, Assignment assignment, Container container) {
        this.score = score;
        this.submitCode = submitCode;
        this.executionResult = executionResult;
        this.student = student;
        this.assignment = assignment;
        this.container = container;
    }
}
