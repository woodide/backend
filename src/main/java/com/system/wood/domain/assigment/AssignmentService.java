package com.system.wood.domain.assigment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public Long save(Assignment assignment) {
        Assignment savedAssignment = assignmentRepository.save(assignment);
        return savedAssignment.getId();
    }

    public void delete(Long assignmentId) {
        if(assignmentRepository.existsById(assignmentId))
            assignmentRepository.deleteById(assignmentId);
        else
            throw new EntityNotFoundException(
                    String.format("id가 %d인 과제가 존재하지 않습니다.", assignmentId)
            );
    }
}
