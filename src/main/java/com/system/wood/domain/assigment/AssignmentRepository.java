package com.system.wood.domain.assigment;

import com.system.wood.domain.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findBySubject(Subject subject);

    Optional<Assignment> findByImageName(String imageName);

}
