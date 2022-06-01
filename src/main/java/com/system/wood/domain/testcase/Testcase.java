package com.system.wood.domain.testcase;

import com.system.wood.domain.assigment.Assignment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Testcase {

    @Id
    @Column(name = "testcase_id")
    @GeneratedValue
    private Long id;

    private String inputUrl;

    private String outputUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    public Testcase(String inputUrl, String outputUrl, Assignment assignment) {
        this.inputUrl = inputUrl;
        this.outputUrl = outputUrl;
        this.assignment = assignment;
    }
}
