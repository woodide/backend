package com.system.wood.web.professor.controller;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.assigment.AssignmentService;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.subject.SubjectRepository;
import com.system.wood.domain.testcase.Testcase;
import com.system.wood.domain.user.User;
import com.system.wood.domain.user.UserRepository;
//import com.system.wood.infra.storage.StorageService;
import com.system.wood.web.assignment.dto.AssignmentReqDto;
import com.system.wood.web.assignment.dto.AssignmentReqDto2;
import com.system.wood.web.professor.dto.SubjectDto;
import com.system.wood.web.professor.service.ProfessorService;
import com.system.wood.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/professor")
public class ProfessorContoller {
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
//    @Autowired
//    private StorageService storageService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private SubjectRepository subjectRepository;

    @Transactional
    @PostMapping("/subject")
    public Subject createSubject(@RequestBody SubjectDto subjectDto){
        System.out.println(subjectDto.toString());
        Subject subject = new Subject();
        subject.setId(subjectDto.getSubjectId());
        subject.setName(subjectDto.getName());
        subject.setCode((subjectDto.getCode()));
        List<User> users = userRepository.findAllById(subjectDto.getStudentsId());
        subject.setUserList(users);
        return professorService.createSubject(subject);
    }

    @Transactional
    @PostMapping("/subject/addStudent")
    public void addStudent(@RequestBody SubjectDto subjectDto){
        Optional<Subject> subj = professorService.findById(subjectDto.getSubjectId());
        if(subj.isEmpty()){
            return ;
        }
        Subject subject = subj.get();
        List<User> users = userRepository.findAllById(subjectDto.getStudentsId());
        subject.setUserList(users);
        professorService.createSubject(subject);
    }

    @Transactional
    @GetMapping("/subject/student")
    public List<User> student(@RequestParam("id") Long subjectId){
        Subject subject = subjectRepository.findOneById(subjectId);
        return subject.getUserList();
    }

    @Transactional
    @PostMapping("/subject/addAssignment")
    public void addAssignment(AssignmentReqDto2 assignmentReqDto){
        Optional<Subject> subj = professorService.findById(assignmentReqDto.getSubjectId());
        if(subj.isEmpty()){
            return ;
        }
    }
}
