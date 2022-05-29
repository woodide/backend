package com.system.wood.web.container.controller;

import com.system.wood.domain.member.Member;
import com.system.wood.web.container.service.WebContainerService;
import com.system.wood.web.container.dto.ContainerDelDto;
import com.system.wood.web.container.dto.ContainerReqDto;
import com.system.wood.web.container.dto.ResponseDto;
import com.system.wood.web.container.dto.ReturnStatus;
import com.system.wood.web.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebContainerController {

    private final WebContainerService webContainerService;
    private final MemberService memberService;

    @ResponseBody
    @PostMapping("/container")
    public ResponseEntity<ResponseDto> createContainer(@RequestBody ContainerReqDto containerReuestDto){

        // TODO: 로그인 가정 JWT 가드 붙이고 해제
        Member member = memberService.findOneById(Long.valueOf(0));
        String containerName = containerReuestDto.getContainerName();

        try {
            webContainerService.createContainer(containerName, member);
            return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.valueOf(201));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResponseDto.of(ReturnStatus.FAIL, e.getMessage()), HttpStatus.valueOf(400));
        }
    }

    @DeleteMapping("/container")
    public ResponseEntity<ResponseDto> deleteContainer(@RequestBody ContainerDelDto containerDeleteDto) {

        // TODO: 로그인 가정 JWT 가드 붙이고 해제
        Member member = memberService.findOneById(Long.valueOf(0));

        // 로그인한 멤버가 컨테이너의 소유자가 아닌 경우에 에러를 던진다.
        webContainerService.validateContainerOwner(member, containerDeleteDto.getContainerId());

        try {
            webContainerService.deleteContainer(containerDeleteDto.getContainerId());
            return new ResponseEntity<>(ResponseDto.getSuccessDto(), HttpStatus.valueOf(204));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ResponseDto.of(ReturnStatus.FAIL, e.getMessage()), HttpStatus.valueOf(400));
        }
    }

    @ResponseBody
    @PostMapping("/docker/build/{lang}")
    public String buildImage(@PathVariable() String lang) throws Exception {
        // 약 5분 소요
        webContainerService.buildImage(lang,"test","7");
        return "succ";
    }
}
