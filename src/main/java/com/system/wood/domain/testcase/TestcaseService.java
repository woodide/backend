package com.system.wood.domain.testcase;

import com.system.wood.domain.assigment.Assignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestcaseService {

    private final TestcaseRepository testcaseRepository;

    public List<Testcase> getTestcases(Assignment assignment) {
        return testcaseRepository.findByAssignment(assignment);
    }

    public Long save(Testcase testcase) {
        Testcase savedTestcase = testcaseRepository.save(testcase);
        return savedTestcase.getId();
    }
}
