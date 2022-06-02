package com.system.wood.web.user.service;

import com.system.wood.domain.User;
import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.professor.ProfessorRepository;
import com.system.wood.domain.student.Student;
import com.system.wood.domain.Token;
import com.system.wood.domain.student.StudentRepository;
import com.system.wood.global.error.BusinessException;
import com.system.wood.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfessorRepository professorRepository;

    public Student findOneById(Long id){
        return studentRepository.findById(id).orElseThrow(
                () -> {
                    throw new EntityNotFoundException(String.format("id가 %d인 학생이 존재하지 않습니다.", id));
                }
        );
    }

    public List<Student> findAll(){
        return studentRepository.findAll();
    }

    public Boolean isDuplicated(String email){
        Optional<Student> member = studentRepository.findByEmail(email);
        Optional<Professor> professor = professorRepository.findByEmail(email);
        if(member.isEmpty() && professor.isEmpty()) return false;
        return true;
    }
    public Professor saveProfessor(Professor professor){
        professor.setPassword(passwordEncoder.encode((professor.getPassword())));
        return professorRepository.save(professor);
    }

    public Student saveStudent(Student student){
        student.setPassword(passwordEncoder.encode((student.getPassword())));
        return studentRepository.save(student);
    }

    public User login(Token.Request request){
        Optional<Student> user = studentRepository.findByEmail(request.getEmail());
        Optional<Professor> professor = professorRepository.findByEmail(request.getEmail());
        if(user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword())){
            return user.get();
        } else if(professor.isPresent() && passwordEncoder.matches(request.getPassword(), professor.get().getPassword())){
            return professor.get();
        } else {
            throw new BusinessException(ErrorCode.UsernameOrPasswordNotFoundException);
        }
    }

    public Student findStudent(String email) {
        return studentRepository.findByEmail(email).orElseThrow(
                () -> {
                    throw new EntityNotFoundException(String.format("email이 %s인 학생이 존재하지 않습니다.", email));
                }
        );
    }

    public Professor findProfessor(String email) {
        return professorRepository.findByEmail(email).orElseThrow(
                () -> {
                    throw new EntityNotFoundException(String.format("email이 %s인 교수가 존재하지 않습니다.", email));
                }
        );
    }
}
