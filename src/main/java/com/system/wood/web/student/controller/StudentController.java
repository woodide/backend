package com.system.wood.web.student.controller;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.assigment.AssignmentService;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.container.ContainerService;
import com.system.wood.domain.report.Report;
import com.system.wood.domain.report.ReportService;
import com.system.wood.domain.result.Result;
import com.system.wood.domain.result.ResultService;
import com.system.wood.domain.student.Student;
import com.system.wood.domain.subject.Subject;
import com.system.wood.domain.subject.SubjectService;
import com.system.wood.infra.GradingService;
import com.system.wood.infra.dto.ResultDto;
import com.system.wood.infra.storage.StorageService;
import com.system.wood.web.container.dto.ResponseDto;
import com.system.wood.web.professor.dto.AssignmentResDto;
import com.system.wood.web.professor.dto.SubjectDto;
import com.system.wood.web.student.dto.AsgnSubmDto;
import com.system.wood.web.student.dto.ReportDto;
import com.system.wood.web.student.dto.ResultResDto;
import com.system.wood.web.student.service.StudentService;
import com.system.wood.web.user.service.UserService;
import com.system.wood.web.user.service.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final ReportService reportService;

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
        Container container = containerService.getContainer(asgnSubmDto.getContainerName());
        Assignment assignment = container.getAssignment();
        // 로그인한 학생이 컨테이너를 소유하고 있는지 확인
        userValidator.validateStudent(email, asgnSubmDto.getContainerName());

        // 파일 복사
        String target = storageService.locateTarget(container, assignment);

        // 채점
        ResultDto gradingDto = gradingService.execute(assignment, container, target);

        // 채점 결과 저장
        resultService.save(gradingDto.toEntity(container.getStudent(), assignment, container));
        containerService.updateCount(container);

        return new ResponseEntity<>(gradingDto, HttpStatus.OK);
    }

    @GetMapping("/subject/assignment/result")
    public ResponseEntity<List<ResultResDto>> getGradingResultList(@AuthenticationPrincipal String email, @PathParam("containerName") String containerName) {
        userValidator.validateStudent(email, containerName);
        Assignment assignment = containerService.getContainer(containerName).getAssignment();
        Student student = userService.findStudent(email);
        List<Result> resultList = resultService.getResultList(student, assignment);
        List<ResultResDto> resultResDtoList = new ArrayList<>();
        for (int idx = 0; idx < resultList.size() ; idx++) {
            ResultResDto resultResDto = ResultResDto.of(assignment.getTargetFileName(), resultList.get(idx), idx + 1);
            resultResDtoList.add(resultResDto);
        }

        return new ResponseEntity<>(resultResDtoList, HttpStatus.OK);
    }

    @GetMapping("/subject/assignment/result/best")
    public ResponseEntity<ResultResDto> getBestGradingResult(@AuthenticationPrincipal String email, @PathParam("containerName") String containerName) {
        userValidator.validateStudent(email, containerName);
        Container container = containerService.getContainer(containerName);
        Assignment assignment = container.getAssignment();
        Student student = userService.findStudent(email);
        List<Result> bestResult = resultService.getBestResultByAsgnAndStud(student, assignment, PageRequest.of(0,1));

        if (bestResult.isEmpty())
            return new ResponseEntity<>(new ResultResDto(), HttpStatus.OK);
        else
            return new ResponseEntity<>(ResultResDto.of(assignment.getTargetFileName(), bestResult.get(0), container.getCount()), HttpStatus.OK);
    }

    @PostMapping("/subject/assignment/report")
    public ResponseEntity<ResponseDto> submitReport(@AuthenticationPrincipal String email, @RequestBody ReportDto reportDto) {
        userValidator.validateStudent(email, reportDto.getContainerName());
        Assignment assignment = containerService.getContainer(reportDto.getContainerName()).getAssignment();
        Student student = userService.findStudent(email);
        reportService.saveOrUpdate(reportDto.toEntity(student,assignment));

        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.OK);
    }

    @GetMapping("/subject/assignment/report")
    public ResponseEntity<ReportDto> getReport(@AuthenticationPrincipal String email, @PathParam("containerName") String containerName) {
        userValidator.validateStudent(email, containerName);
        Assignment assignment = containerService.getContainer(containerName).getAssignment();
        Student student = userService.findStudent(email);
        Optional<Report> savedReport = reportService.getReport(student, assignment);
        String report = savedReport.isPresent() ? savedReport.get().getContent() : "";

        return new ResponseEntity<>(new ReportDto(report), HttpStatus.OK);
    }
}
