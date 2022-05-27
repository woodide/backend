package com.system.wood.web.assignment.controller;

import com.system.wood.domain.assigment.AssignmentService;
import com.system.wood.domain.member.Member;
import com.system.wood.domain.member.MemberService;
import com.system.wood.infra.storage.StorageService;
import com.system.wood.web.assignment.dto.AssignmentReqDto;
import com.system.wood.web.container.dto.ResponseDto;
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
    private final MemberService memberService;

    @ResponseBody
    @PostMapping(value = "/api/setAssignment")
    public ResponseEntity<ResponseDto> registerZipFile(AssignmentReqDto assignmentReqDto) {

        Member member = memberService.getMember(1000L);

        // 파일을 저장장치에 저장
        String uploadUrl = storageService.store(assignmentReqDto.getMultipartFile());

        // db에 과제 정보 저장
        assignmentService.save(assignmentReqDto.toEntity(uploadUrl, member));
        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.valueOf(200));
    }
}
