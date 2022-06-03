package com.system.wood.domain.subject;

import com.system.wood.domain.student.Student;
import com.system.wood.web.professor.dto.StudResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public List<StudResDto> listStudentResDto(String code) {
        return getSubject(code).getStudToSubjList().stream().map(studToSubj -> {
            Student student = studToSubj.getStudent();
            return new StudResDto(student.getStudentNumber(), student.getEmail(), student.getUsername());
        }).collect(Collectors.toList());
    }

    public Subject getSubject(String code) {
        return subjectRepository.findByCode(code).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("code가 %s인 과목이 존재하지 않습니다.", code));
        });
    }
}
