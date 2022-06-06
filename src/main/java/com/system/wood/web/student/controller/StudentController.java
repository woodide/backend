package com.system.wood.web.student.controller;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.container.ContainerService;
import com.system.wood.domain.student.Student;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.subject.SubjectService;
import com.system.wood.infra.storage.StorageService;
import com.system.wood.web.container.dto.ResponseDto;
import com.system.wood.web.professor.dto.AssignmentResDto;
import com.system.wood.web.professor.dto.SubjectDto;
import com.system.wood.web.student.dto.AsgnSubmDto;
import com.system.wood.web.student.service.StudentService;
import com.system.wood.web.user.service.UserService;
import com.system.wood.web.user.service.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;
    private final SubjectService subjectService;
    private final ContainerService containerService;
    private final UserValidator userValidator;
    private final StorageService storageService;

    @GetMapping("/subject")
    public ResponseEntity<List<SubjectDto>> getSubjectList(@AuthenticationPrincipal String email) {
        Student student = userService.findStudent(email);
        List<SubjectDto> subjectDtoList = studentService.getSubjectDtoList(student);
        return new ResponseEntity<>(subjectDtoList, HttpStatus.OK);
    }

    @GetMapping("/subject/assignment")
    public ResponseEntity<List<AssignmentResDto>> getAssignmentList(@RequestParam("code") String code) {
        Subject subject = subjectService.getSubject(code);
        List<AssignmentResDto> assignmentDtoList = studentService.getAssignmentDtoList(subject);
        return new ResponseEntity<>(assignmentDtoList, HttpStatus.OK);
    }

    @PostMapping("/subject/assignment/submit")
    public ResponseEntity<ResponseDto> submitAssignment(@AuthenticationPrincipal String email, @RequestBody AsgnSubmDto asgnSubmDto) {
        Container container = containerService.getContainer(asgnSubmDto.getPortNum());
        Assignment assignment = container.getAssignment();
        // 로그인한 학생이 컨테이너를 소유하고 있는지 확인
        userValidator.validateStudent(email, asgnSubmDto.getPortNum());

        // 파일 복사
        storageService.locateTarget(container, assignment);

        // 채점


        return new ResponseEntity<ResponseDto>(ResponseDto.getSuccessDto(), HttpStatus.OK);
    }
}
