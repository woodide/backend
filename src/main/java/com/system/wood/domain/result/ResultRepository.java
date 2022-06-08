package com.system.wood.domain.result;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.student.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByStudentAndAssignment(Student student, Assignment assignment);

    List<Result> findTop1ByStudentAndAssignmentOrderByScoreDesc(Student student, Assignment assignment);

    @Query("select r from Result r join fetch r.container where r.student = ?1 and r.assignment = ?2 order by r.score desc")
    List<Result> findResultListByStudentAndAssignment(Student student, Assignment assignment, Pageable pageable);
}
