package com.system.wood.infra;

import com.system.wood.domain.Member;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.container.ContainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InfraService {

    private final ContainerService containerService;

    @Transactional
    public Container createContainer(String containerName, Member member) throws IOException {

        // todo: 나중에 만들 과제 테이블의 id를 저장하자.
        Integer pgID = 82;
        Integer portNum = containerService.getAvailablePortNum();
        String path = getPath();

        String command = new StringBuilder().append("docker run -d --containerName=")
                .append(containerName)
                .append(" -e PUID=").append(member.getId())
                .append(" -e PGID=").append(pgID)
                .append(" -e TZ=Asia/Seoul")
                .append(" -e SUDO_PASSWORD=password")
                .append(" -p ")
                .append(portNum).append(":8443")
                .append(" -v :"+path+":/config ")
                .append(" --restart unless-stopped linuxserver/code-server")
                .toString();

        Process process = Runtime.getRuntime()
                .exec(command);

        String dockerContainerId = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        log.info(dockerContainerId);
        // 절대경로는 DB에 저장

        return Container.builder()
                .portNum(portNum)
                .dockerContainerId(dockerContainerId)
                .containerName(containerName)
                .member(member)
                .build();
    }

    // todo: directory를 생성하고 container에 마운팅할 절대경로를 만드는 로직을 추가
    public String getPath() {
        return "~/Desktop/wood/test_env2/data/code-server";
    }
}
