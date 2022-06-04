package com.system.wood.web.user.controller;

import com.system.wood.domain.User;
import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.student.Student;
import com.system.wood.jwt.JwtTokenProvider;
import com.system.wood.domain.Role;
import com.system.wood.domain.Token;
import com.system.wood.web.professor.dto.StudResDto;
import com.system.wood.web.professor.service.ProfessorService;
import com.system.wood.web.user.dto.LoginResDto;
import com.system.wood.web.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProfessorService professorService;

    @GetMapping("/list/student")
    public List<StudResDto> allStudent(){
        return userService.findAll().stream().map(student -> new StudResDto(student.getStudentNumber(), student.getEmail(), student.getUsername())).collect(Collectors.toList());
    }

    @GetMapping("/list/professor")
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
            LoginResDto loginResDto= (user instanceof Professor)
                    ? new LoginResDto(token, Boolean.TRUE)
                    : new LoginResDto(token, Boolean.FALSE);
            return new ResponseEntity<>(loginResDto, HttpStatus.OK);
        }
        catch (Exception e){
            throw e;
        }
    }
}
