package com.system.wood.domain.subject;


import com.system.wood.domain.BaseTimeEnity;
import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.studtosubj.StudToSubj;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Subject extends BaseTimeEnity {

    @Id
    @GeneratedValue
    @Column(name = "subject_id")
    private Long id;

    private String name;

    private String code;

    @OneToMany(mappedBy = "subject")
    private List<StudToSubj> studToSubjList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    public Subject(String name, String code, Professor professor) {
        this.name = name;
        this.code = code;
        this.professor = professor;
    }

    @Override
    // note: 두 엔티티가 비영속 상태인 경우에는 true를 반환한다
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.getId());
    }
}
