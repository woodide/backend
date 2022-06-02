package com.system.wood.web.assignment.controller;

import com.system.wood.domain.assigment.AssignmentService;
import com.system.wood.domain.user.User;
import com.system.wood.infra.storage.StorageService;
import com.system.wood.web.assignment.dto.AssignmentReqDto;
import com.system.wood.web.container.dto.ResponseDto;
import com.system.wood.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AssignmentController {

    private final StorageService storageService;
    private final AssignmentService assignmentService;
    private final UserService userService;

    @ResponseBody
    @PostMapping(value = "/api/setAssignment")
    public ResponseEntity<ResponseDto> registerZipFile(AssignmentReqDto assignmentReqDto) {

        // TODO: 로그인 가정 JWT 가드 붙이고 해제
        User user = userService.findOneById(Long.valueOf(0));

        // 파일을 저장장치에 저장
        String uploadUrl = storageService.store(assignmentReqDto.getMultipartFile());

        // db에 과제 정보 저장
        assignmentService.save(assignmentReqDto.toEntity(uploadUrl, user));
        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.valueOf(200));
    }
}
