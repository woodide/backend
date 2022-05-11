package com.system.wood.domain;

import com.sun.istack.NotNull;
import com.system.wood.domain.container.Container;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Container> containerList = new ArrayList<>();

    @Builder

    public Member(String email, String password, String username, Role role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }
}
