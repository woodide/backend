package com.system.wood.domain.user;

import com.sun.istack.NotNull;
import com.system.wood.domain.Role;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.usertosubject.UserToSubj;
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
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    @NotNull
    private String password;

    @Column
    @NotNull
    private String student_id;

    @Column
    @NotNull
    private String username;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @OneToMany(mappedBy = "user")
    private List<UserToSubj> userToSubjList = new ArrayList<>();

    @Column
    @OneToMany(mappedBy = "user")
    private List<Container> containerList = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }

    @Builder
    public User(String email, String password, String username, Role role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }
}
