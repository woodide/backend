package com.system.wood.infra;

import com.system.wood.domain.container.Container;
import com.system.wood.domain.member.Member;
import com.system.wood.global.error.BusinessException;
import com.system.wood.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
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

    private static final String stop ="docker stop ";
    private static final String rmCommand = "docker rm ";

    @Value("${file.parent-path}")
    private String parentPath;

    @Transactional
    public Container createContainer(String containerName, Member member) throws IOException, BusinessException {

        Integer pgID = 82; // 고민: 나중에 만들 과제 테이블의 id를 저장하자.
        Integer portNum = findFreePort();
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

        if(error.isEmpty()) {
            log.info("컨테이너 생성: "+ output);
        } else {
            log.info("error message: "+error);
            log.info("command: "+ command);
            throw new BusinessException(ErrorCode.CANNOT_CREATE_CONTAINER);
        }

        return Container.of(portNum, output, containerName, path,member);
    }

    @Transactional
    public void deleteContainer(String dockerContainerId, String path) throws IOException {
        String stopCommand = stop + dockerContainerId;
        String removeCommand = rmCommand + dockerContainerId;

        Process stopProcess = Runtime.getRuntime()
                .exec(stopCommand);
        String output = new BufferedReader(new InputStreamReader(stopProcess.getInputStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
        String error = new BufferedReader(new InputStreamReader(stopProcess.getErrorStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        if(error.isEmpty()) {
            log.info("컨테이너 중지: "+ output);
        } else {
            log.info("error message: "+error);
            log.info("command: "+ stopCommand);
            throw new BusinessException(ErrorCode.CANNOT_STOP_CONTAINER);
        }

        Process rmProcess = Runtime.getRuntime()
                .exec(removeCommand);
        output = new BufferedReader(new InputStreamReader(rmProcess.getInputStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
        error = new BufferedReader(new InputStreamReader(rmProcess.getErrorStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        if(error.isEmpty()) {
            log.info("컨테이너 삭제: "+ output);
        } else {
            log.info("error message: "+error);
            log.info("command: "+ rmProcess);
            throw new BusinessException(ErrorCode.CANNOT_KILL_CONTAINER);
        }

        // 컨테이너에서 사용하던 폴더 삭제
        deleteDirectory(path);
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

    private void makeDirectory(String path) throws IOException {

        Path dirPath = Paths.get(path);
        Files.createDirectories(dirPath);
    }

    private void deleteDirectory(String path) throws IOException {

        FileUtils.deleteDirectory(new File(path));
    }

    private static int findFreePort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            int port = socket.getLocalPort();
            try {
                socket.close();
            } catch (IOException e) {
                // Ignore IOException on close()
            }
            return port;
        } catch (IOException e) {
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
        throw new IllegalStateException("Could not find a free TCP/IP port to start embedded Jetty HTTP Server on");
    }
}
