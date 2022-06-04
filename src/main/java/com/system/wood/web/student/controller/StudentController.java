package com.system.wood.web.student.controller;

import com.system.wood.domain.student.Student;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.subject.SubjectService;
import com.system.wood.web.professor.dto.AssignmentResDto;
import com.system.wood.web.professor.dto.SubjectDto;
import com.system.wood.web.student.service.StudentService;
import com.system.wood.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;
    private final SubjectService subjectService;

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
}
