package com.system.wood.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executors;

@Slf4j
@RestController
public class DockerController {

    @GetMapping("/docker/create")
    public void createContainer(){
        try {
            // dockerService에 비즈니스 로직을 옮길 예정
            String command = "docker run -d --name=code-server2 " +
                    "-e PUID=82 -e PGID=82 -e TZ=Asia/Seoul " +
                    "-e SUDO_PASSWORD=password -p 8442:8443 " +
                    "-v :~/Desktop/wood/test_env2/data/code-server:/config " +
                    "--restart unless-stopped linuxserver/code-server";
            Process process = Runtime.getRuntime()
                    .exec(command);
            StreamGobbler streamGobbler =
                    new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = process.waitFor();
            assert exitCode == 0;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("IOEeception 발생");
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("InterruptedException 발생");
        }
    }
}
