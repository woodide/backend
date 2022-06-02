package com.system.wood.domain.usertosubject;

import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.user.User;

import javax.persistence.*;

@Entity
@Table(name = "user_to_subj")
public class UserToSubj {

    @Id @GeneratedValue
    @Column(name = "user_to_subj_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    // 필요한 필드들 자유롭게 추가
}
