package com.system.wood.web.assignment.controller;

import com.system.wood.domain.assigment.AssignmentService;
import com.system.wood.domain.member.Member;
import com.system.wood.infra.storage.StorageService;
import com.system.wood.web.assignment.dto.AssignmentReqDto;
import com.system.wood.web.container.dto.ResponseDto;
import com.system.wood.web.container.service.WebContainerService;
import com.system.wood.web.member.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AssignmentController {

    private final StorageService storageService;
    private final AssignmentService assignmentService;
    private final MemberService memberService;
    private final WebContainerService webContainerService;

    @Value("${file.dockerImage-path}")
    private String imagePath;

    @ResponseBody
    @PostMapping(value = "/professor/setAssignment")
    public ResponseEntity<ResponseDto> setAssignment(AssignmentReqDto assignmentReqDto) throws Exception {

        // TODO: 로그인 가정 JWT 가드 붙이고 해제
        Member member = memberService.findOneById(Long.valueOf(0));

        // 하드디스크에 스켈레톤코드 저장
        String uploadUrl = storageService.unzipFile(assignmentReqDto.getMultipartFile());

        // 도커 이미지 생성(5분 소요)
        String imageStoredName = UUID.randomUUID().toString();
        String imageUrl = imagePath + imageStoredName;
        webContainerService.buildImage(assignmentReqDto.getLanguage(), imageStoredName,assignmentReqDto.getLanguageVersion());

        // db에 과제 정보 저장
        assignmentService.save(assignmentReqDto.toEntity(uploadUrl, imageUrl, member));
        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.valueOf(200));
    }
//
//    @ResponseBody
//    @PostMapping(value = "/any/test")
//    public ResponseEntity<ResponseDto> isArchive(TestDto testDto) throws Exception {
//
//        String target = storageService.unzipFile(testDto.getMultipartFile());
//        log.info(target);
//        return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.valueOf(200));
//    }
//
//    @Getter
//    @Setter
//    static class TestDto{
//        private MultipartFile multipartFile;
//    }
}
