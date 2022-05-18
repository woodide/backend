package com.system.wood.infra;

import com.system.wood.domain.member.Member;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.container.ContainerService;
import com.system.wood.global.error.BusinessException;
import com.system.wood.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InfraService {

    private final ContainerService containerService;
    private static final String stopCommand ="docker stop ";
    private static final String rmCommand = "docker rm ";

    @Value("${file.parent-path}")
    private String parentPath;

    @Transactional
    public Container createContainer(String containerName, Member member) throws IOException {

        Integer pgID = 82; // 고민: 나중에 만들 과제 테이블의 id를 저장하자.
        Integer portNum = containerService.getAvailablePortNum();
        String path = parentPath + containerName + portNum;

        // container를 생성하는 command 작성
        String command = createCommand(member.getId(), pgID, portNum, containerName);

        // directory 생성
        makeDirectory(path);

        // command 실행과 stdout, stderr 결과 획득
        Process process = Runtime.getRuntime()
                .exec(command);
        String output = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
        String error = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        // todo: 에러 처리
        if(error.isEmpty()) {
            log.info("컨테이너 생성: "+ output);
        } else {
            throw new BusinessException(ErrorCode.CANNOT_CREATE_CONTAINER);
        }

        return Container.of(portNum, output, containerName, path,member);
    }

    public void makeDirectory(String parentPath) throws IOException {

        Path path = Paths.get(parentPath);
        Files.createDirectories(path);
    }

    @Transactional
    public void deleteContainer(String dockerContainerId) throws IOException {
        Process stopProcess = Runtime.getRuntime()
                .exec(stopCommand+dockerContainerId);

        Process rmProcess = Runtime.getRuntime()
                .exec(rmCommand+dockerContainerId);
    }

    private String createCommand(Long memberId, Integer pgID, Integer portNum, String containerName) {
        String path = parentPath + containerName + portNum;

        return new StringBuilder().append("docker run -d --name=")
                .append(containerName)
                .append(" -e PUID=").append(memberId)
                .append(" -e PGID=").append(pgID)
                .append(" -e TZ=Asia/Seoul")
                .append(" -e SUDO_PASSWORD=password")
                .append(" -p ")
                .append(portNum).append(":8443")
                .append(" -v :" + path + ":/config ")
                .append(" --restart unless-stopped linuxserver/code-server")
                .toString();
    }
}
