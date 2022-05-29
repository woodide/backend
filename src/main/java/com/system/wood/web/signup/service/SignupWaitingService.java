package com.system.wood.web.signup.service;

import com.system.wood.domain.member.Member;
import com.system.wood.domain.member.MemberRepository;
import com.system.wood.domain.signup.SignupWaiting;
import com.system.wood.domain.signup.SignupWaitingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SignupWaitingService {

    private SignupWaitingRepository signupWaitingRepository;
    private MemberRepository memberRepository;
    @Autowired
    public SignupWaitingService(SignupWaitingRepository signupWaitingRepository, MemberRepository memberRepository){
        this.signupWaitingRepository = signupWaitingRepository;
        this.memberRepository = memberRepository;
    }
    public Boolean isDuplicated(String email){
        Optional<SignupWaiting> waiting = signupWaitingRepository.findByEmail(email);
        if(waiting.isEmpty()) return false;
        return true;
    }
    public List<SignupWaiting> findAll(){
        List<SignupWaiting> waitings = signupWaitingRepository.findAll();
        return waitings;
    }

    public Member approveOne(Long id){
        SignupWaiting waiting = signupWaitingRepository.findById(id).get();

        Member member = new Member();
        member.setEmail(waiting.getEmail());
        member.setPassword(waiting.getPassword());
        member.setUsername(waiting.getUsername());
        member.setRole(waiting.getRole());
        memberRepository.save(member);

        return member;
    }

    public List<SignupWaiting> approveAll(){
        List<SignupWaiting> waitings = signupWaitingRepository.findAll();
        for(SignupWaiting waiting:waitings){
            Member member = new Member();
            member.setEmail(waiting.getEmail());
            member.setPassword(waiting.getPassword());
            member.setUsername(waiting.getUsername());
            member.setRole(waiting.getRole());
            memberRepository.save(member);
        }
        signupWaitingRepository.deleteAll();
        return waitings;
    }

    public void deleteById(Long id){
        signupWaitingRepository.deleteById(id);
    }

}
