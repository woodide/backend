package com.system.wood.infra.dockercontainer;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.student.Student;
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
public class DockerContainerService {
    private static final String stop ="docker stop ";
    private static final String rmCommand = "docker rm ";


    @Value("${file.container-path}")
    private String containerPath;

    @Transactional
    public Container createContainer(String containerName, String imageName, Student user, Assignment assignment) throws IOException, BusinessException, InterruptedException {
        Integer PUID = 82;
        Integer PGID = 82;
        Integer portNum = findFreePort();
        String path = containerPath + containerName + portNum;

        // container를 생성하는 command 작성
        String command = createCommand(PUID, PGID, portNum, containerName, imageName);

        // command 실행과 stdout, stderr 결과 획득
        Process process = Runtime.getRuntime()
                .exec(command);
        String output = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
        String error = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        if(error.isEmpty()) {
            // 컨테이너 생성까지 기다리기
            log.info("컨테이너 생성: "+ output);
            String logsCmd = createLogs(containerName);
            log.info(logsCmd);
                Process logProcess = Runtime.getRuntime()
                        .exec(logsCmd);
                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(logProcess.getInputStream()));
                System.out.println("Here is the standard output of the command:\n");
                String s = null;
                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                    log.info("컨테이너 기다리기: "+ s);
                    if(s.contains("Not serving HTTPS")) { // 컨테이너 생성 끝
                        log.info("컨테이너 생성 완료: "+ s);
                        logProcess.destroy();
                        break;
                    }
                }
                process.waitFor();
        } else {
            log.info("error message: "+error);
            log.info("command: "+ command);
            throw new BusinessException(ErrorCode.CANNOT_CREATE_CONTAINER);
        }

        return Container.of(portNum, output, containerName, path, user, assignment);
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

    private String createLogs(String containerName) {
        return new StringBuilder()
                .append("docker logs ")
                .append(containerName)
                .append(" --follow")
                .toString();
    }

    private String createCommand(Integer PUID, Integer PGID, Integer portNum, String containerName, String imageName) {
        String path = containerPath + containerName + portNum;

        return new StringBuilder().append("docker run -d --name=")
                .append(containerName)
                .append(" -e PUID=").append(PUID)
                .append(" -e PGID=").append(PGID)
                .append(" -e TZ=Asia/Seoul")
                .append(" -e SUDO_PASSWORD=password")
                .append(" -p ")
                .append(portNum).append(":8443")
                .append(" -v " + path + ":/config ")
                .append(" --restart unless-stopped ")
                .append(imageName)
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
