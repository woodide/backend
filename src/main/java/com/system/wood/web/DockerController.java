package com.system.wood.web;

import com.system.wood.domain.Member;
import com.system.wood.infra.InfraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DockerController {

    private final DockerService dockerService;

    @GetMapping("/docker/create")
    public void createContainer(){

        Member member = new Member(); // mock data, 인자로 받을 예정
        String containerName = "name"; // mock data, request로 받을 예정

        try {
            dockerService.createContainer(containerName, member);
            // todo: return success
        } catch (IOException e) {
            e.printStackTrace();
            // todo: return fail
        }
    }
}
