package com.system.wood.web.professor.controller;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.assigment.AssignmentService;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.user.User;
import com.system.wood.domain.user.UserRepository;
import com.system.wood.infra.storage.StorageService;
import com.system.wood.web.assignment.dto.AssignmentReqDto;
import com.system.wood.web.assignment.dto.AssignmentReqDto2;
import com.system.wood.web.professor.dto.SubjectDto;
import com.system.wood.web.professor.service.ProfessorService;
import com.system.wood.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/professor")
public class ProfessorContoller {
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/subject")
    public Subject createSubject(@RequestBody SubjectDto subjectDto){
        Subject subject = new Subject();
        subject.setName(subjectDto.getName());
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
        List<User> students = subject.getUserList();
        List<User> users = userRepository.findAllById(subjectDto.getStudentsId());
        for(User i:users){
            students.add(i);
        }
        subject.setUserList(students);
        professorService.createSubject(subject);
    }

    @Transactional
    @PostMapping("/subject/addAssignment")
    public void addAssignment(AssignmentReqDto2 assignmentReqDto){
        Optional<Subject> subj = professorService.findById(assignmentReqDto.getSubjectId());
        if(subj.isEmpty()){
            return ;
        }

        // TODO: 로그인 가정 JWT 가드 붙이고 해제
        User user = userService.findOneById(Long.valueOf(0));

        // 파일을 저장장치에 저장
        String uploadUrl = storageService.store(assignmentReqDto.getMultipartFile());

        // db에 과제 정보 저장
        Assignment assignment = assignmentReqDto.toEntity(uploadUrl, user);
        assignmentService.save(assignment);

        Subject subject = subj.get();
        List<Assignment> assignments = subject.getAssignmentList();
        assignments.add(assignment);
        subject.setAssignmentList(assignments);

        professorService.createSubject(subject);
    }
}
