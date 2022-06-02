package com.system.wood.domain.subject;


import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.user.User;
import com.system.wood.domain.usertosubject.UserToSubj;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Subject {

    @Id
    @GeneratedValue
    @Column(name = "subject_id")
    private Long id;

    private String name;

    private String code;

    @ManyToMany
    private List<User> userList = new ArrayList<>();

    @OneToMany(mappedBy = "subject")
    private List<UserToSubj> userToSubjList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    public Subject(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
