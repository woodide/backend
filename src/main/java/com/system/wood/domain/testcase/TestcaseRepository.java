package com.system.wood.domain.testcase;

import com.system.wood.domain.assigment.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestcaseRepository extends JpaRepository<Testcase, Long> {

    List<Testcase> findByAssignment(Assignment assignment);
}
