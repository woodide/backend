package com.system.wood.web.student.controller;

import com.system.wood.domain.student.Student;
import com.system.wood.web.student.dto.SubjectResDto;
import com.system.wood.web.student.service.StudentService;
import com.system.wood.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;

    @GetMapping("/subject")
    public ResponseEntity<List<SubjectResDto>> getSubjectList(@AuthenticationPrincipal String email) {
        Student student = userService.findStudent(email);
        return new ResponseEntity<>(studentService.listSubject(student), HttpStatus.OK);
    }
}
