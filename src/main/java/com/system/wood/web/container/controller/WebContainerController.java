package com.system.wood.web.container.controller;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.assigment.AssignmentService;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.student.Student;
import com.system.wood.web.container.dto.*;
import com.system.wood.web.container.service.WebContainerService;
import com.system.wood.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebContainerController {

    private final WebContainerService webContainerService;
    private final UserService userService;
    private final AssignmentService assignmentService;
    @ResponseBody
    @GetMapping("/container")
    public ResponseEntity getContainer(@AuthenticationPrincipal String email,@RequestParam("imageName") String imageName){
        Student student = userService.findStudent(email);
        String containerName = imageName + student.getStudentNumber();
        Assignment assignment = assignmentService.getAssignment(imageName);
        Container container = webContainerService.getContainer(containerName);
        return new ResponseEntity<>(new ResponseContainerDto(container.getPortNum(),containerName,assignment.getAssignmentName(),assignment.getDueDate(),assignment.getDescription()), HttpStatus.valueOf(201));
    }


    @ResponseBody
    @PostMapping("/container")
    public ResponseEntity createContainer(@AuthenticationPrincipal String email, @RequestBody ContainerReqDto containerReuestDto){

        Student student = userService.findStudent(email);
        String containerName = containerReuestDto.getImageName() + student.getStudentNumber();
        String imageName = containerReuestDto.getImageName();
        Assignment assignment = assignmentService.getAssignment(containerReuestDto.getImageName());

        try {
            Container container =  webContainerService.createContainer(containerName, imageName, student, assignment);

            return new ResponseEntity<>(new ResponseContainerDto(container.getPortNum(),containerName,assignment.getAssignmentName(),assignment.getDueDate(),assignment.getDescription()), HttpStatus.valueOf(201));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResponseDto.of(ReturnStatus.FAIL, e.getMessage()), HttpStatus.valueOf(400));
        }
    }

    @DeleteMapping("/container")
    public ResponseEntity<ResponseDto> deleteContainer(@AuthenticationPrincipal String email, @RequestBody ContainerDelDto containerDeleteDto) {

        Student student = userService.findStudent(email);

        // 로그인한 멤버가 컨테이너의 소유자가 아닌 경우에 에러를 던진다.
        webContainerService.validateContainerOwner(student, containerDeleteDto.getContainerId());

        try {
            webContainerService.deleteContainer(containerDeleteDto.getContainerId());
            return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.valueOf(204));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResponseDto.of(ReturnStatus.FAIL, e.getMessage()), HttpStatus.valueOf(400));
        }
    }
}
