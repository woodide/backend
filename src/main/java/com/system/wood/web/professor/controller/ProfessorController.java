package com.system.wood.web.professor.controller;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.assigment.AssignmentService;
import com.system.wood.domain.container.ContainerService;
import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.result.Result;
import com.system.wood.domain.result.ResultService;
import com.system.wood.domain.student.Student;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.subject.SubjectService;
import com.system.wood.domain.testcase.Testcase;
import com.system.wood.domain.testcase.TestcaseService;
import com.system.wood.infra.storage.StorageService;
import com.system.wood.web.professor.dto.*;
import com.system.wood.web.container.dto.ResponseDto;
import com.system.wood.web.container.dto.ReturnStatus;
import com.system.wood.web.container.service.WebContainerService;
import com.system.wood.web.professor.service.ProfessorService;
import com.system.wood.web.user.service.UserService;
import com.system.wood.web.user.service.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/professor")
@RequiredArgsConstructor
public class ProfessorController {
    private final ProfessorService professorService;
    private final UserService userService;
    private final AssignmentService assignmentService;
    private final WebContainerService webContainerService;
    private final TestcaseService testcaseService;
    private final StorageService storageService;
    private final SubjectService subjectService;
    private final UserValidator userValidator;
    private final ResultService resultService;
    private final ContainerService containerService;

    @GetMapping("/subject")
    public ResponseEntity<List<SubjectDto>> listSubjects(@AuthenticationPrincipal String email){
        Professor professor = userService.findProfessor(email);
        List<SubjectDto> subjectList = subjectService.getSubjectList(professor);

        return new ResponseEntity<>(subjectList, HttpStatus.OK);
    }

    @PostMapping("/subject")
    public ResponseEntity<ResponseDto> createSubject(@AuthenticationPrincipal String email, @RequestBody SubjectDto subjectDto){
        Professor professor = userService.findProfessor(email);
        Subject subject = subjectDto.toEntity(professor);
        professorService.createSubject(subject);

        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.OK);
    }

    @PostMapping("/subject/addStudent")
    public ResponseEntity<ResponseDto> addStudent(@AuthenticationPrincipal String email, @RequestBody StudReqDto studReqDto){
        Subject subject = subjectService.getSubject(studReqDto.getSubjectCode());
        userValidator.validateProfessor(email,subject);
        professorService.saveStudentList(studReqDto.getStudentNumberList(), subject);

        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.OK);
    }

    @GetMapping("/subject/student")
    public ResponseEntity<List<StudDto>> listStudentList(@RequestParam("code") String code){
        return new ResponseEntity<>(subjectService.listStudentResDto(code), HttpStatus.OK);
    }

    @GetMapping("/subject/student/result")
    public ResponseEntity<List<StudResDto>> listResultList(@RequestParam("imageName") String imageName){
        Assignment assignment = assignmentService.getAssignment(imageName);
        Subject subject = assignment.getSubject();
        if (assignment.getSubject().getId() != subject.getId()) {
            throw new RuntimeException("과목에 포함되지 않는 과제입니다.");
        }

        List<StudResDto> studResDtoList = subject.getStudToSubjList().stream().map(studToSubj -> {
            List<Result> resultOptional = resultService.getBestResultByAsgnAndStud(studToSubj.getStudent(), assignment, PageRequest.of(0,1));
            if (resultOptional.isEmpty()) {
                Student student = studToSubj.getStudent();
                return new StudResDto(false, student.getStudentNumber(), student.getUsername(), 0.0D, 0, "미제출", "미제출");
            } else {
                Result result = resultOptional.get(0);
                Student student = studToSubj.getStudent();
                return new StudResDto(true, student.getStudentNumber(), student.getUsername(), result.getScore(), result.getContainer().getCount(), result.getExecutionResult(), result.getSubmitCode());
            }
        }).collect(Collectors.toList());

        return new ResponseEntity<>(studResDtoList, HttpStatus.OK);
    }

    @GetMapping("/subject/assignment")
    public ResponseEntity<List<AssignmentResDto>> listAssignments(@RequestParam("code") String code){
        Subject subject = subjectService.getSubject(code);
        List<AssignmentResDto> dtoList = assignmentService.getAssignmentList(subject).stream().map(assignment -> new AssignmentResDto(assignment.getAssignmentName(), assignment.getDescription(), assignment.getImageName(), assignment.getDueDate())).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("/subject/addAssignment")
    public ResponseEntity<ResponseDto> addAssignment(@AuthenticationPrincipal String email, AssignmentReqDto assignmentReqDto){

        // 과목 찾기
        Subject subject = subjectService.getSubject(assignmentReqDto.getSubjectCode());

        // 교수가 해당 과목을 담당하고 있는지 확인
        userValidator.validateProfessor(email, subject);

        // 하드디스크에 테스트케이스와 스켈레톤 코드 저장
        String inputUrl = storageService.storeTestcase(assignmentReqDto.getTestInput());
        String outputUrl = storageService.storeTestcase(assignmentReqDto.getTestOutput());
        String uploadUrl = storageService.unzipFile(assignmentReqDto.getMultipartFile());
        File uploadFile = new File(uploadUrl);
        if(!new File(uploadUrl).isDirectory()) uploadUrl = uploadFile.toPath().getParent().toString();

        // 도커 이미지 생성(5분 소요)
        String imageStoredName = assignmentReqDto.getAssignmentName().toLowerCase().replaceAll(" ","_").concat(RandomString.make(2).toLowerCase(Locale.ROOT));
        try {
            webContainerService.buildImage(assignmentReqDto.getLanguage(), imageStoredName, assignmentReqDto.getLanguageVersion());
        } catch (Exception e) {
            log.info("도커 이미지 생성 중 에러 발생");
            e.printStackTrace();

            return new ResponseEntity<>(ResponseDto.of(ReturnStatus.FAIL, "도커 이미지 생성 중 에러 발생"), HttpStatus.valueOf(500));
        }

        // db에 과제와 테스트케이스 정보 저장
        Assignment savedAssignment = assignmentService.save(assignmentReqDto.toEntity(uploadUrl, imageStoredName, subject));
        testcaseService.save(new Testcase(inputUrl, outputUrl, savedAssignment));

        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.valueOf(200));
    }

    // 학생 리스트에서 학생 결과를 조회하는 거임
//     @GetMapping("/subject/assignment/result")

}
