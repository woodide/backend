package com.system.wood.web.student.controller;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.assigment.AssignmentService;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.container.ContainerService;
import com.system.wood.domain.result.Result;
import com.system.wood.domain.result.ResultService;
import com.system.wood.domain.student.Student;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.subject.SubjectService;
import com.system.wood.infra.GradingService;
import com.system.wood.infra.dto.ResultDto;
import com.system.wood.infra.storage.StorageService;
import com.system.wood.web.professor.dto.AssignmentResDto;
import com.system.wood.web.professor.dto.SubjectDto;
import com.system.wood.web.student.dto.AsgnSubmDto;
import com.system.wood.web.student.dto.ResultResDto;
import com.system.wood.web.student.service.StudentService;
import com.system.wood.web.user.service.UserService;
import com.system.wood.web.user.service.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;
    private final SubjectService subjectService;
    private final ContainerService containerService;
    private final UserValidator userValidator;
    private final StorageService storageService;
    private final GradingService gradingService;
    private final ResultService resultService;
    private final AssignmentService assignmentService;

    @GetMapping("/subject")
    public ResponseEntity<List<SubjectDto>> getSubjectList(@AuthenticationPrincipal String email) {
        Student student = userService.findStudent(email);
        List<SubjectDto> subjectDtoList = studentService.getSubjectDtoList(student);
        return new ResponseEntity<>(subjectDtoList, HttpStatus.OK);
    }

    @GetMapping("/subject/assignment")
    public ResponseEntity<List<AssignmentResDto>> getAssignmentList(@RequestParam("code") String code) {
        Subject subject = subjectService.getSubject(code);
        List<AssignmentResDto> assignmentDtoList = studentService.getAssignmentDtoList(subject);
        return new ResponseEntity<>(assignmentDtoList, HttpStatus.OK);
    }

    @PostMapping("/subject/assignment/submit")
    public ResponseEntity<ResultDto> submitAssignment(@AuthenticationPrincipal String email, @RequestBody AsgnSubmDto asgnSubmDto) {
        Container container = containerService.getContainer(asgnSubmDto.getPortNum());
        Assignment assignment = container.getAssignment();
        // 로그인한 학생이 컨테이너를 소유하고 있는지 확인
        userValidator.validateStudent(email, asgnSubmDto.getPortNum());

        // 파일 복사
        String target = storageService.locateTarget(container, assignment);

        // 채점
        ResultDto gradingDto = gradingService.execute(assignment, container, target);

        // 채점 결과 저장
        resultService.save(gradingDto.toEntity(container.getStudent(), assignment));

        return new ResponseEntity<>(gradingDto, HttpStatus.OK);
    }

    @GetMapping("/subject/assignment/result")
    public ResponseEntity<List<ResultResDto>> getGradingResultList(@AuthenticationPrincipal String email, @RequestParam("imageName") String imageName) {
        Assignment assignment = assignmentService.getAssignment(imageName);
        Student student = userService.findStudent(email);
        List<Result> resultList = resultService.getResultList(student, assignment);
        List<ResultResDto> resultDtoList = resultList.stream().map(ResultResDto::from).collect(Collectors.toList());

        return new ResponseEntity<>(resultDtoList, HttpStatus.OK);
    }

    @GetMapping("/subject/assignment/result/best")
    public ResponseEntity<ResultResDto> getBestGradingResult(@AuthenticationPrincipal String email, @RequestParam("imageName") String imageName) {
        Assignment assignment = assignmentService.getAssignment(imageName);
        Student student = userService.findStudent(email);
        Result bestResult = resultService.getBestResult(student, assignment);

        return new ResponseEntity<>(ResultResDto.from(bestResult), HttpStatus.OK);
    }
}
