package com.system.wood.web.user.controller;

import com.system.wood.domain.User;
import com.system.wood.domain.professor.Professor;
import com.system.wood.global.error.BusinessException;
import com.system.wood.global.error.ErrorCode;
import com.system.wood.jwt.JwtTokenProvider;
import com.system.wood.domain.Token;
import com.system.wood.web.container.dto.ResponseDto;
import com.system.wood.web.professor.dto.StudResDto;
import com.system.wood.web.professor.service.ProfessorService;
import com.system.wood.web.user.dto.LoginResDto;
import com.system.wood.web.user.dto.ProfSignUpDto;
import com.system.wood.web.user.dto.ProfessorDto;
import com.system.wood.web.user.dto.StudSignupDto;
import com.system.wood.web.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProfessorService professorService;

//    @GetMapping("/list/student")
//    public ResponseEntity<List<StudResDto>> allStudent() {
//        List<StudResDto> studResDtoList = userService.findAll().stream().map(student -> new StudResDto(student.getStudentNumber(), student.getEmail(), student.getUsername())).collect(Collectors.toList());
//        return new ResponseEntity<>(studResDtoList, HttpStatus.OK);
//    }

    @GetMapping("/list/professor")
    public ResponseEntity<List<ProfessorDto>> allProfessor() {
        List<ProfessorDto> professorDtoList = professorService.findAll().stream().map(professor -> new ProfessorDto(professor.getEmail(), professor.getUsername())).collect(Collectors.toList());
        return new ResponseEntity<>(professorDtoList, HttpStatus.OK);
    }

    @PostMapping("/signup/student")
    public ResponseEntity<ResponseDto> createStudent(@RequestBody StudSignupDto studSignupDto) {
        if (userService.isDuplicated(studSignupDto.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATED_EMAIL);
        }
        userService.saveStudent(studSignupDto.toEntity());
        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.OK);
    }

    @PostMapping("/signup/professor")
    public ResponseEntity<ResponseDto> createProfessor(@RequestBody ProfSignUpDto profSignUpDto) {
        if (userService.isDuplicated(profSignUpDto.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATED_EMAIL);
        }
        userService.saveProfessor(profSignUpDto.toEntity());
        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Token.Request request) throws Exception {
        try {
            User user = userService.login(request);
            String token = JwtTokenProvider.generateToken(user.getEmail(), user.getRole().toString());
            LoginResDto loginResDto = (user instanceof Professor)
                    ? new LoginResDto(token, user.getUsername(), user.getEmail(), Boolean.TRUE)
                    : new LoginResDto(token, user.getUsername(), user.getEmail(), Boolean.FALSE);
            return new ResponseEntity<>(loginResDto, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }
}
