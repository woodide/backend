package com.system.wood.domain.assigment;

import com.system.wood.domain.subject.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    @Transactional
    public Assignment save(Assignment assignment) {
        Assignment savedAssignment = assignmentRepository.save(assignment);
        return savedAssignment;
    }

    @Transactional
    public void delete(Long assignmentId) {
        if(assignmentRepository.existsById(assignmentId))
            assignmentRepository.deleteById(assignmentId);
        else
            throw new EntityNotFoundException(
                    String.format("id가 %d인 과제가 존재하지 않습니다.", assignmentId)
            );
    }

    public Assignment getAssignment(Long id) {
        return assignmentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("id가 %d인 과제가 존재하지 않습니다.", id)
                ));
    }

    public Assignment getAssignment(String imageName) {
        return assignmentRepository.findByImageName(imageName).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("이미지 이름이 %s인 과제가 존재하지 않습니다.", imageName)
                ));
    }

    public List<Assignment> getAssignmentList(Subject subject) {
        return assignmentRepository.findBySubject(subject);
    }
}
