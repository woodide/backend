package com.system.wood.infra;

import lombok.RequiredArgsConstructor;
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
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DockerCompileService {

    @Value("${file.parent-path}")
    private String parentPath;


    public void GCC(String imageName, String version) throws IOException  {
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
        makeDockerfile(body);
        String cmd = makeBuildCommand(imageName);
        runBuild(cmd);
    }

    public void Python(String imageName, String version) throws IOException {
        String body = "FROM linuxserver/code-server\n" +
                "RUN sudo apt-get update\n" +
                "RUN sudo apt-get install -y make build-essential libssl-dev zlib1g-dev libbz2-dev \\\n" +
                "    libreadline-dev libsqlite3-dev wget curl llvm libncurses5-dev libncursesw5-dev \\\n" +
                "    xz-utils tk-dev\n" +
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
        makeDockerfile(body);
        String cmd = makeBuildCommand(imageName);
        runBuild(cmd);
    }


    private void makeDockerfile(String dockerBody) throws IOException {
        Path dirPath = Paths.get(parentPath , "build");
        if(!Files.isDirectory(dirPath))
            Files.createDirectory(dirPath);
        Path filePath = Paths.get(parentPath , "build", "DockerFile");
        Files.writeString(filePath, dockerBody, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }


    public String runBuild(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime()
                    .exec(command);
            String output = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            return output;
        } catch (IOException e) {
            String error = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            return error;
        }
    }

    public String makeBuildCommand(String imageName) {
        return new StringBuilder().append("docker build --tag ").append(imageName).append(" . ").toString();
    }

}
