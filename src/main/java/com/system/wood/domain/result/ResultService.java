package com.system.wood.domain.result;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.student.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;

    @Transactional
    public Long save(Result result) {
        Result savedGrade = resultRepository.save(result);
        return savedGrade.getId();
    }

    public List<Result> getResultList(Student student, Assignment assignment) {
        return resultRepository.findByStudentAndAssignment(student, assignment);
    }

    public List<Result> getBestResult(Student student, Assignment assignment) {
        return resultRepository.findTop1ByStudentAndAssignmentOrderByScoreDesc(student, assignment);
    }
}
