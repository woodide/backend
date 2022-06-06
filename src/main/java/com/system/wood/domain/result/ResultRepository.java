package com.system.wood.domain.result;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByStudentAndAssignment(Student student, Assignment assignment);
}
