package com.system.wood.web.container;

import com.system.wood.domain.member.Member;
import com.system.wood.domain.member.MemberService;
import com.system.wood.web.container.dto.ContainerDelDto;
import com.system.wood.web.container.dto.ContainerReqDto;
import com.system.wood.web.container.dto.ContainerResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebContainerController {

    private final WebContainerService webContainerService;
    private final MemberService memberService;

    @ResponseBody
    @PostMapping("/docker/create")
    public ResponseEntity<ContainerResDto> createContainer(@RequestBody ContainerReqDto containerReuestDto){

        // 로그인한 멤버로 가정
        Member member = memberService.getMember(1000L);
        String containerName = containerReuestDto.getContainerName();

        try {
            webContainerService.createContainer(containerName, member);
            return new ResponseEntity<>(ContainerResDto.getSuccess(), HttpStatus.valueOf(201));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ContainerResDto.getFail(), HttpStatus.valueOf(201));
        }
    }

    @PostMapping("/docker/delete")
    public ResponseEntity<ContainerResDto> deleteContainer(@RequestBody ContainerDelDto containerDeleteDto) {

        // 로그인한 멤버로 가정
        Member member = memberService.getMember(2000L);

        // 로그인한 멤버가 컨테이너의 소유자가 아닌 경우에 에러를 던진다.
        webContainerService.validateContainerOwner(member, containerDeleteDto.getContainerId());

        try {
            webContainerService.deleteContainer(containerDeleteDto.getContainerId());
            return new ResponseEntity<>(ContainerResDto.getSuccess(), HttpStatus.valueOf(204));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(ContainerResDto.getFail(), HttpStatus.valueOf(201));
        }
    }
}
