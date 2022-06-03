package com.system.wood.domain.professor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository <Professor,Long> {

    Optional<Professor> findByEmail(String email);

}
