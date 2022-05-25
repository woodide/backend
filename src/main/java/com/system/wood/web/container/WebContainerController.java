package com.system.wood.web.container;

import com.system.wood.domain.member.Member;
import com.system.wood.domain.member.MemberService;
import com.system.wood.web.container.dto.ContainerDelDto;
import com.system.wood.web.container.dto.ContainerReqDto;
import com.system.wood.web.container.dto.ResponseDto;
import com.system.wood.web.container.dto.ReturnStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
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
    @PostMapping("/docker/create")
    public ResponseEntity<ResponseDto> createContainer(@RequestBody ContainerReqDto containerReuestDto){

        // 로그인한 멤버로 가정
        Member member = memberService.getMember(1000L);
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

    @PostMapping("/docker/delete")
    public ResponseEntity<ResponseDto> deleteContainer(@RequestBody ContainerDelDto containerDeleteDto) {

        // 로그인한 멤버로 가정
        Member member = memberService.getMember(2000L);

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
    public String deleteContainer(@PathVariable() String lang) throws IOException {
        webContainerService.buildImage(lang,"test","3.6.8");
        return "succ";
    }
}
