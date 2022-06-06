package com.system.wood.web.user.service;

import com.system.wood.domain.container.Container;
import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.student.Student;
import com.system.wood.domain.subject.Subject;
import com.system.wood.global.error.BusinessException;
import com.system.wood.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public void validateStudent(String email, Integer portNum) {
        boolean isNotOwner = true;

        Student student = userService.findStudent(email);
        List<Container> containerList = student.getContainerList();
        for (Container tmp : containerList) {
            if(tmp.getPortNum().equals(portNum))
                isNotOwner = false;
        }
        if(isNotOwner) throw new BusinessException(ErrorCode.IS_NOT_OWNER);
    }
}
