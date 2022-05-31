package com.system.wood.web.member.controller;

import com.system.wood.domain.member.Member;
import com.system.wood.jwt.JwtTokenProvider;
import com.system.wood.jwt.UserAuthentication;
import com.system.wood.domain.Role;
import com.system.wood.domain.signup.SignupWaiting;
import com.system.wood.domain.Token;
import com.system.wood.web.member.service.MemberService;
import com.system.wood.web.signup.service.SignupWaitingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    SignupWaitingService signupWaitingService;

    @GetMapping("/member")
    public List<Member> listAll(){
        return memberService.findAll();
    }

    @PostMapping("/signup_s")
    public String createStudent(@RequestBody SignupWaiting signupWaiting){
        if(signupWaitingService.isDuplicated(signupWaiting.getEmail()) ||
                memberService.isDuplicated(signupWaiting.getEmail())){
            return "email is duplicated";
        }
        signupWaiting.setRole(Role.STUDENT);
        memberService.saveStudnt(signupWaiting);
        return "success";
    }

    @PostMapping("/signup_p")
    public String createProfessor(@RequestBody Member member){
        member.setRole(Role.PROFESSOR);
        memberService.saveProfessor(member);
        return "success";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Token.Request request) throws Exception {

        try {
            Member member = memberService.login(request);
            if(member==null) throw new Exception("user not found");
            String token = JwtTokenProvider.generateToken(member.getEmail(), member.getRole().toString());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        catch (Exception e){
            throw e;
        }
    }

    @GetMapping("professor")
    public String professor(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.toString();
    }

    @GetMapping("student")
    public String student(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.toString();
    }

    @GetMapping("any")
    public String any(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.toString();
    }
}
