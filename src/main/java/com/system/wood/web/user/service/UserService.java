package com.system.wood.web.user.service;

import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.professor.ProfessorRepository;
import com.system.wood.domain.user.User;
import com.system.wood.domain.Token;
import com.system.wood.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProfessorRepository professorRepository;


    public User findOneById(Long Id){ return userRepository.findOneById(Id);}

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Boolean isDuplicated(String email){
        Optional<User> member = userRepository.findByEmail(email);
        if(member.isEmpty()) return false;
        return true;
    }
    public Professor saveProfessor(Professor professor){
        professor.setPassword(passwordEncoder.encode((professor.getPassword())));
        return professorRepository.save(professor);
    }

    public User saveStudnt(User user){
        user.setPassword(passwordEncoder.encode((user.getPassword())));
        return userRepository.save(user);
    }

    public User login(Token.Request request){
        Optional<User> mem = userRepository.findByEmail(request.getEmail());
        System.out.println(mem.get());
        if(mem.isEmpty()) return null;
        User user = mem.get();
        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return user;
        }
        else return null;
    }

    public Professor loginProfessor(Token.Request request){
        Optional<Professor> mem = professorRepository.findByEmail(request.getEmail());
        System.out.println(mem.get());
        if(mem.isEmpty()) return null;
        Professor professor = mem.get();
        if(passwordEncoder.matches(request.getPassword(), professor.getPassword())){
            return professor;
        }
        else return null;
    }
}
