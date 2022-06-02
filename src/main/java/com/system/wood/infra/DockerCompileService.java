package com.system.wood.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DockerCompileService {

    @Value("${file.dockerImage-path}")
    private String imagePath;

    public void GCC(String imageName, String version) throws Exception {
        String body =  "FROM linuxserver/code-server\n" +
                "RUN sudo apt-get update\n" +
                "RUN sudo apt-get install -y make build-essential software-properties-common\n" +
                "RUN sudo add-apt-repository -y ppa:ubuntu-toolchain-r/test\n" +
                "RUN sudo apt-get update\n" +
                "\n" +
                String.format("ENV VERSION=%s\n",version) +
                "ENV SKELETON_CODE /home/skeleton/pa2.c\n" +
                "\n" +
                "RUN sudo apt-get install -y gcc-${VERSION}\n" +
                "RUN sudo update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-${VERSION} 60 --slave /usr/bin/g++ g++ /usr/bin/g++-${VERSION}";
        makeDockerfile(body, imageName);
        String cmd = makeBuildCommand(imageName);
        System.out.println("cmd " + cmd);
        runBuild(cmd);
    }

    public void Python(String imageName, String version) throws Exception {
        String body = "FROM linuxserver/code-server\n" +
                "RUN sudo apt-get update\n" +
                "RUN sudo apt-get install -y make build-essential libssl-dev zlib1g-dev libbz2-dev libreadline-dev libsqlite3-dev wget curl llvm libncurses5-dev libncursesw5-dev xz-utils tk-dev\n" +
                "ENV HOME=\"/config\"\n" +
                String.format("ENV PYTHON_VERSION=%s\n",version) +
                "ENV SKELETON_CODE /home/skeleton/pa2.c\n" +
                "WORKDIR $HOME\n" +
                "\n" +
                "RUN git clone https://github.com/pyenv/pyenv.git .pyenv\n" +
                "\n" +
                "ENV PYENV_ROOT=\"$HOME/.pyenv\"\n" +
                "ENV PATH=\"$PYENV_ROOT/shims:$PYENV_ROOT/bin:$PATH\"\n" +
                "\n" +
                "RUN pyenv install ${PYTHON_VERSION}\n" +
                "RUN pyenv global ${PYTHON_VERSION}\n";
        makeDockerfile(body, imageName);
        String cmd = makeBuildCommand(imageName);
        System.out.println("cmd " + cmd);
        runBuild(cmd);
    }


    private void makeDockerfile(String dockerBody, String imageName) throws IOException {
        Path dirPath = Paths.get(imagePath);
        if(!Files.isDirectory(dirPath))
            Files.createDirectory(dirPath);
        Path filePath = Paths.get(imagePath, imageName);
        Files.writeString(filePath, dockerBody, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }


    public boolean runBuild(String command) throws Exception {
        Process process = Runtime.getRuntime()
                .exec(command);
        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(process.getErrorStream()));
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        boolean isSuccess = false;
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
            if(s.contains("exporting to image")) { // 이미지 빌드 끝
                isSuccess = true;
            }
        }
        if(isSuccess) {
            return true;
        }
        else {
            throw new Exception("Docker Build Error");
        }
    }

    public String makeBuildCommand(String imageName) {
        return new StringBuilder().append("docker build --no-cache --tag ").append(imageName).append(" ").append(Paths.get(imagePath, "build")).toString();
    }
}
