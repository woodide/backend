package com.system.wood.domain.subject;

import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.student.Student;
import com.system.wood.web.professor.dto.StudResDto;
import com.system.wood.web.professor.dto.SubjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

//    public List<StudResDto> listStudentResDto(String code) {
//        return getSubject(code).getStudToSubjList().stream().map(studToSubj -> {
//            Student student = studToSubj.getStudent();
//            return new StudResDto(student.getStudentNumber(), student.getUsername());
//        }).collect(Collectors.toList());
//    }

    public List<SubjectDto> getSubjectList(Professor professor) {
        return subjectRepository.findByProfessor(professor).stream().map(subject -> new SubjectDto(subject.getName(), subject.getCode())).collect(Collectors.toList());
    }

    public Subject getSubject(String code) {
        return subjectRepository.findByCode(code).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("code가 %s인 과목이 존재하지 않습니다.", code));
        });
    }
}
