package com.system.wood.domain.container;

import com.sun.istack.NotNull;
import com.system.wood.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Container {

    @Id @GeneratedValue
    @Column(name = "container_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer portNum;

    @Column(name = "docker_container_id", unique = true, nullable = false)
    private String dockerContainerId;

    @NotNull
    private String containerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Container(Integer portNum, String dockerContainerId, Member member, String containerName) {
        this.portNum = portNum;
        this.dockerContainerId = dockerContainerId;
        this.containerName = containerName;
        this.member = member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
