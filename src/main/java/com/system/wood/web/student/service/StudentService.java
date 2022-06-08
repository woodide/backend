package com.system.wood.web.student.service;

import com.system.wood.domain.assigment.AssignmentService;
import com.system.wood.domain.student.Student;
import com.system.wood.domain.studtosubj.StudToSubj;
import com.system.wood.domain.subject.Subject;
import com.system.wood.web.professor.dto.AssignmentResDto;
import com.system.wood.web.professor.dto.SubjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {

    private final AssignmentService assignmentService;

    public List<SubjectDto> getSubjectDtoList(Student student) {

        return student.getStudToSubjList()
                .stream()
                .map((StudToSubj::getSubject))
                .map((subject -> new SubjectDto(subject.getName(), subject.getCode())))
                .collect(Collectors.toList());
    }

    public List<AssignmentResDto> getAssignmentDtoList(Subject subject) {
        return assignmentService.getAssignmentList(subject)
                .stream()
                .map(assignment -> new AssignmentResDto(assignment.getAssignmentName(), assignment.getDescription(), assignment.getImageName(), assignment.getExistsReport(), assignment.getDueDate()))
                .collect(Collectors.toList());

    }
}
