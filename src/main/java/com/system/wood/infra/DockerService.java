package com.system.wood.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class DockerService {

    public String createContainer() throws InterruptedException, IOException {
        String containerId = "";
        String command = "docker run -d --name=code-server2 " +
                "-e PUID=82 -e PGID=82 -e TZ=Asia/Seoul " +
                "-e SUDO_PASSWORD=password -p 8442:8443 " +
                "-v :~/Desktop/wood/test_env2/data/code-server:/config " +
                "--restart unless-stopped linuxserver/code-server";
        Process process = Runtime.getRuntime()
                .exec(command);
        // todo: process.getInputStream()을 String으로 변환하는 로직 추가
        // todo: 안 쓰는 포트넘버를 구하는 로직 추가
        // PUID는 memberId, PGID는 과제의 아이디
        // 절대경로는 DB에 저장

        return containerId;
    }
}
