package com.system.wood.domain.professor;

import com.sun.istack.NotNull;
import com.system.wood.domain.Role;
import com.system.wood.domain.User;
import com.system.wood.domain.subject.Subject;
import lombok.Builder;
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
public class Professor extends User {

    @Id
    @GeneratedValue
    @Column(name = "professor_id")
    private Long id;

    @OneToMany(mappedBy = "professor")
    private List<Subject> subjectList = new ArrayList<>();

    @Override
    public String toString() {
        return super.toString();
    }

    @Builder
    public Professor(String email, String password, String username, Role role) {
        super(email, password, username, role);
    }

    public void setPassword(String password) {
        super.setPassword(password);
    }
}
