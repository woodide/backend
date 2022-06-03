package com.system.wood.web.professor.controller;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.assigment.AssignmentService;
import com.system.wood.domain.professor.Professor;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.subject.SubjectRepository;
import com.system.wood.domain.subject.SubjectService;
import com.system.wood.domain.testcase.Testcase;
import com.system.wood.domain.testcase.TestcaseService;
import com.system.wood.domain.student.Student;
import com.system.wood.domain.student.StudentRepository;
import com.system.wood.infra.storage.StorageService;
import com.system.wood.web.professor.dto.AssignmentReqDto;
import com.system.wood.web.container.dto.ResponseDto;
import com.system.wood.web.container.dto.ReturnStatus;
import com.system.wood.web.container.service.WebContainerService;
import com.system.wood.web.professor.dto.StudReqDto;
import com.system.wood.web.professor.dto.StudResDto;
import com.system.wood.web.professor.dto.SubjectDto;
import com.system.wood.web.professor.service.ProfessorService;
import com.system.wood.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/professor")
@RequiredArgsConstructor
public class ProfessorController {
    private final ProfessorService professorService;
    private final StudentRepository studentRepository;
    private final UserService userService;
    private final AssignmentService assignmentService;
    private final WebContainerService webContainerService;
    private final TestcaseService testcaseService;
    private final SubjectRepository subjectRepository;
    private final StorageService storageService;
    private final SubjectService subjectService;

    @Transactional
    @PostMapping("/subject")
    public ResponseEntity<ResponseDto> createSubject(@AuthenticationPrincipal String email, @RequestBody SubjectDto subjectDto){
        Professor professor = userService.findProfessor(email);
        Subject subject = subjectDto.toEntity(professor);
        professorService.createSubject(subject);

        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/subject/addStudent")
    public ResponseEntity<ResponseDto> addStudent(@RequestBody StudReqDto studReqDto){

        Subject subject = subjectService.getSubject(studReqDto.getSubjectCode());
        professorService.addStudentList(studReqDto.getStudentNumberList(), subject);

        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/subject/student")
    public ResponseEntity<List<StudResDto>> student(@RequestParam("code") String code){
        return new ResponseEntity<>(subjectService.listStudentResDto(code), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/subject/addAssignment")
    public ResponseEntity<ResponseDto> addAssignment(@AuthenticationPrincipal String email, AssignmentReqDto assignmentReqDto){

        // 과목 찾기
        Subject subject = professorService.findById(assignmentReqDto.getSubjectId());

        // 유저 확인
        Student user = userService.findStudent(email);

        // 하드디스크에 테스트케이스 저장
        String inputUrl = storageService.storeTestcase(assignmentReqDto.getTestInput());
        String outputUrl = storageService.storeTestcase(assignmentReqDto.getTestOutput());

        // 하드디스크에 스켈레톤코드 저장
        String uploadUrl = storageService.unzipFile(assignmentReqDto.getMultipartFile());

        // 도커 이미지 생성(5분 소요)
        String imageStoredName = UUID.randomUUID().toString();
        try {
            webContainerService.buildImage(assignmentReqDto.getLanguage(), imageStoredName, assignmentReqDto.getLanguageVersion());
        } catch (Exception e) {
            log.info("도커 이미지 생성 중 에러 발생");
            e.printStackTrace();

            return new ResponseEntity<>(ResponseDto.of(ReturnStatus.FAIL, "도커 이미지 생성 중 에러 발생"), HttpStatus.valueOf(500));
        }

        // db에 과제와 테스트케이스 정보 저장
        Assignment savedAssignment = assignmentService.save(assignmentReqDto.toEntity(uploadUrl, imageStoredName));
        testcaseService.save(new Testcase(inputUrl, outputUrl, savedAssignment));

        // 과목에 과제를 연관
        savedAssignment.setSubject(subject);

        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.valueOf(200));
    }
}
