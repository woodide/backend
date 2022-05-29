package com.system.wood.web.member.service;

import com.system.wood.domain.member.Member;
import com.system.wood.domain.signup.SignupWaiting;
import com.system.wood.domain.Token;
import com.system.wood.domain.member.MemberRepository;
import com.system.wood.domain.signup.SignupWaitingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;
    private SignupWaitingRepository signupWaitingRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, SignupWaitingRepository signupWaitingRepository, PasswordEncoder passwordEncoder){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.signupWaitingRepository = signupWaitingRepository;
    }

    public Member findOneById(Long Id){ return memberRepository.findOneById(Id);}

    public Optional<Member> findByUsername(String username){ return memberRepository.findByUsername(username);}

    public Optional<Member> findByIdPw(String email){ return memberRepository.findByEmail(email);}

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    public Boolean isDuplicated(String email){
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isEmpty()) return false;
        return true;
    }
    public Member saveProfessor(Member member){
        member.setPassword(passwordEncoder.encode((member.getPassword())));
        return memberRepository.save(member);
    }

    public SignupWaiting saveStudnt(SignupWaiting signupWaiting){
        signupWaiting.setPassword(passwordEncoder.encode((signupWaiting.getPassword())));
        return signupWaitingRepository.save(signupWaiting);
    }

    public Member login(Token.Request request){
        Optional<Member> mem = memberRepository.findByEmail(request.getEmail());
        System.out.println(mem.get());
        if(mem.isEmpty()) return null;
        Member member = mem.get();
        if(passwordEncoder.matches(request.getPassword(), member.getPassword())){
            return member;
        }
        else return null;
    }
}
