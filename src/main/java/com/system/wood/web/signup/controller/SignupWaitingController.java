package com.system.wood.web.signup.controller;

import com.system.wood.domain.member.Member;
import com.system.wood.domain.signup.SignupWaiting;
import com.system.wood.web.signup.service.SignupWaitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SignupWaitingController {

    private SignupWaitingService signupWaitingService;

    @Autowired
    public SignupWaitingController(SignupWaitingService signupWaitingService){
        this.signupWaitingService = signupWaitingService;
    }

    @GetMapping("/signupwaiting")
    public List<SignupWaiting> listAll(){
        return signupWaitingService.findAll();
    }

    @GetMapping("signupapprove")
    public void approveOne(@RequestParam Long id){
        Member member = signupWaitingService.approveOne(id);
        signupWaitingService.deleteById(id);
        System.out.println(member);
    }

    @GetMapping("signupapproveall")
    public List<SignupWaiting> approveAll(){
        return signupWaitingService.approveAll();
    }
}
