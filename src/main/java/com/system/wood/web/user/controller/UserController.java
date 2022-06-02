package com.system.wood.web.user.controller;

import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.user.User;
import com.system.wood.jwt.JwtTokenProvider;
import com.system.wood.domain.Role;
import com.system.wood.domain.Token;
import com.system.wood.web.professor.service.ProfessorService;
import com.system.wood.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProfessorService professorService;

    @GetMapping("/user/student")
    public List<User> allStudent(){
        return userService.findAll();
    }

    @GetMapping("/user/professor")
    public List<Professor> allProfessor(){
        return professorService.findAll();
    }

    @PostMapping("/signup_s")
    public String createStudent(@RequestBody User user){
        user.setRole(Role.STUDENT);
        if(userService.isDuplicated(user.getEmail())){
            return "email is duplicated";
        }
        userService.saveStudnt(user);
        return "success";
    }

    @PostMapping("/signup_p")
    public String createProfessor(@RequestBody Professor professor){
        professor.setRole(Role.PROFESSOR);
        userService.saveProfessor(professor);
        return "success";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Token.Request request) throws Exception {

        try {
            User user = userService.login(request);
            if(user ==null) throw new Exception("user not found");
            String token = JwtTokenProvider.generateToken(user.getEmail(), user.getRole().toString());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        catch (Exception e){
            throw e;
        }
    }
}
