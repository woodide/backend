package com.system.wood.web.professor.service;

import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.professor.ProfessorRepository;
import com.system.wood.domain.student.Student;
import com.system.wood.domain.student.StudentRepository;
import com.system.wood.domain.studtosubj.StudToSubj;
import com.system.wood.domain.studtosubj.StudToSubjRepository;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.subject.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudToSubjRepository studToSubjRepository;

    @Transactional
    public Subject findById(Long id) {
        return subjectRepository.findById(id).orElseThrow(
                () -> {
                    throw new EntityNotFoundException(String.format("id가 %d인 Subject가 존재하지 않습니다.", id));
                }
        );
    }

    @Transactional
    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    @Transactional
    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public void addStudentList(List<String> studentNumberList, Subject subject) {
        List<Student> studentList = studentRepository.findByStudentNumberIn(studentNumberList);
        studentList.stream()
                .map(student -> new StudToSubj(student, subject))
                .forEach(studToSubjRepository::save);
    }

}
