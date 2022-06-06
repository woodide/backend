package com.system.wood.domain.container;

import com.sun.istack.NotNull;
import com.system.wood.domain.BaseTimeEnity;
import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.student.Student;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Container extends BaseTimeEnity {

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
    @JoinColumn(name = "user_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @Builder
    public Container(Integer portNum, String dockerContainerId, String containerName, String path, Student student, Assignment assignment) {
        this.portNum = portNum;
        this.dockerContainerId = dockerContainerId;
        this.containerName = containerName;
        this.path = path;
        this.student = student;
        this.assignment = assignment;
    }

    public static Container of(Integer portNum, String dockerContainerId, String containerName, String path, Student student, Assignment assignment) {
        return Container.builder()
                .portNum(portNum)
                .dockerContainerId(dockerContainerId)
                .containerName(containerName)
                .student(student)
                .path(path)
                .assignment(assignment)
                .build();
    }

    public void setUser(Student student) {
        this.student = student;
    }
}
