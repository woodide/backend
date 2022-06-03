package com.system.wood.domain.studtosubj;

import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.student.Student;

import javax.persistence.*;

@Entity
@Table(name = "stud_to_subj")
public class StudToSubj {

    @Id @GeneratedValue
    @Column(name = "stud_to_subj_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    // 필요한 필드들 자유롭게 추가
}
