package com.system.wood.web.user.service;

import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.subject.Subject;
import com.system.wood.global.error.BusinessException;
import com.system.wood.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserValidator {

    private final UserService userService;

    public void validateProfessor(String email, Subject subject) {
        Professor professor = userService.findProfessor(email);
        if(!professor.getSubjectList().contains(subject))
            throw new BusinessException(ErrorCode.PROFESSOR_SUBJECT_NOT_MATCHED);
    }
}
