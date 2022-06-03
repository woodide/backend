package com.system.wood.domain.subject;

import com.system.wood.domain.studtosubj.StudToSubj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findOneById(Long id);

    Optional<Subject> findByCode(String code);
}
