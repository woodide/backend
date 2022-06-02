package com.system.wood.web.professor.service;

import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.professor.ProfessorRepository;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.subject.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private ProfessorRepository professorRepository;

    @Transactional
    public Optional<Subject> findById(Long id){
        return subjectRepository.findById(id);
    }

    @Transactional
    public List<Professor> findAll(){
        return professorRepository.findAll();
    }

    @Transactional
    public Subject createSubject(Subject subject){
        return subjectRepository.save(subject);
    }

}
