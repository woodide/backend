package com.system.wood.web.user.controller;

import com.system.wood.domain.User;
import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.student.Student;
import com.system.wood.jwt.JwtTokenProvider;
import com.system.wood.domain.Role;
import com.system.wood.domain.Token;
import com.system.wood.web.professor.service.ProfessorService;
import com.system.wood.web.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProfessorService professorService;

    @GetMapping("/student")
    public List<Student> allStudent(){
        return userService.findAll();
    }

    @GetMapping("/professor")
    public List<Professor> allProfessor(){
        return professorService.findAll();
    }

    @PostMapping("/signup/student")
    public String createStudent(@RequestBody Student student){
        student.setRole(Role.STUDENT);
        if(userService.isDuplicated(student.getEmail())){
            return "email is duplicated";
        }
        userService.saveStudent(student);
        return "success";
    }

    @PostMapping("/signup/professor")
    public String createProfessor(@RequestBody Professor professor){
        professor.setRole(Role.PROFESSOR);
        userService.saveProfessor(professor);
        return "success";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Token.Request request) throws Exception {

        try {
            User user = userService.login(request);
            String token = JwtTokenProvider.generateToken(user.getEmail(), user.getRole().toString());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        catch (Exception e){
            throw e;
        }
    }
}
