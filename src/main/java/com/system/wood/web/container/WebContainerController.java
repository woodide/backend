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

        // spring security에서 @AuthenticationPrincipal로 사용하여
        // UserDetails 객체를 인자로 받을 수 있다.
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
    public void deleteContainer(ContainerDelDto containerDeleteDto) {

        // todo: 삭제를 요청하는 유저가 컨테이너를 소유하는지 확인하는 로직이 필요
        try {
            webContainerService.deleteContainer(containerDeleteDto.getContainerId());
            // return success
        } catch (IOException e) {
            e.printStackTrace();
            // return fail
        }
    }
}
