package com.system.wood.web.student.service;

import com.system.wood.domain.assigment.AssignmentRepository;
import com.system.wood.domain.student.Student;
import com.system.wood.domain.studtosubj.StudToSubj;
import com.system.wood.web.professor.dto.SubjectDto;
import com.system.wood.web.student.dto.SubjectResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final AssignmentRepository assignmentRepository;

    public List<SubjectDto> listSubject(Student student) {

        return student.getStudToSubjList()
                .stream()
                .map((StudToSubj::getSubject))
                .map((subject -> new SubjectDto(subject.getName(), subject.getCode())))
                .collect(Collectors.toList());
    }
}
