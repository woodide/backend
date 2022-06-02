package com.system.wood.domain.subject;


import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.user.User;
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

    @OneToMany
    private List<Assignment> assignmentList = new ArrayList<>();
}
