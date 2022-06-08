package com.system.wood.domain.report;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByStudentAndAssignment(Student student, Assignment assignment);

    boolean existsByStudentAndAssignment(Student student, Assignment assignment);

}
