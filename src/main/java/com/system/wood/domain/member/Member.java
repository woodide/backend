package com.system.wood.domain.member;

import com.sun.istack.NotNull;
import com.system.wood.domain.Role;
import com.system.wood.domain.container.Container;
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
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private Long classNumber;

    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
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
