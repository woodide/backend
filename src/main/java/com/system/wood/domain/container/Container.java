package com.system.wood.domain.container;

import com.sun.istack.NotNull;
import com.system.wood.domain.member.Member;
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

    @NotNull
    private String path; // containerName+portNum로 생성

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Container(Integer portNum, String dockerContainerId, String containerName, String path, Member member) {
        this.portNum = portNum;
        this.dockerContainerId = dockerContainerId;
        this.containerName = containerName;
        this.path = path;
        this.member = member;
    }

    public static Container of(Integer portNum, String dockerContainerId, String containerName, String path, Member member) {
        return Container.builder()
                .portNum(portNum)
                .dockerContainerId(dockerContainerId)
                .containerName(containerName)
                .member(member)
                .path(path)
                .build();
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
