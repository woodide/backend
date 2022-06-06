package com.system.wood.domain.studtosubj;

import com.system.wood.domain.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudToSubjRepository extends JpaRepository<StudToSubj, Long> {
    void deleteBySubject(Subject subject);
}
