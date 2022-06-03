package com.system.wood.web.student.service;

import com.system.wood.domain.assigment.AssignmentRepository;
import com.system.wood.domain.student.Student;
import com.system.wood.web.student.dto.SubjectResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final AssignmentRepository assignmentRepository;

    public List<SubjectResDto> listSubject(Student student) {

        return student.getStudToSubjList()
                .stream()
                .map((studToSubj -> studToSubj.getSubject()))
                .map((subject -> new SubjectResDto(subject.getName(), subject.getCode())))
                .collect(Collectors.toList());
    }
}
