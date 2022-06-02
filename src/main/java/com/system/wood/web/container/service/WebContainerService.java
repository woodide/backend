package com.system.wood.web.container.service;

import com.system.wood.domain.user.User;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.container.ContainerService;
import com.system.wood.global.error.BusinessException;
import com.system.wood.global.error.ErrorCode;
import com.system.wood.infra.dockercontainer.DockerContainerService;
import com.system.wood.infra.DockerCompileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebContainerService {
    private final ContainerService containerService;
    private final DockerContainerService infraService;
    private final DockerCompileService dockerCompileService;

    @Transactional
    public void createContainer(String containerName, User user) throws IOException {
        Container newContainer = infraService.createContainer(containerName, user);

        // todo: 과제에서 기본 세팅 파일을 움직이는 로직이 필요함.
        containerService.save(newContainer);
    }

    @Transactional
    public void deleteContainer(Long containerId) throws IOException {

        // 폴더 경로 획득
        Container container = containerService.getContainerById(containerId);
        String path = container.getPath();

        // db에서 container 정보 삭제
        String dockerContainerId = containerService.removeContainer(containerId);

        // docker에서 container 삭제 & 컨테이너가 사용한던 폴더를 삭제
        infraService.deleteContainer(dockerContainerId, path);
    }

    public void validateContainerOwner(User user, Long containerId) throws BusinessException{
        if (!user.equals(containerService.getContainerById(containerId).getUser())) {
            log.info(String.format("error: member(id:%d)는 컨테이너(id:%d)를 소유하지 않습니다.", user.getId(), containerId));
            throw new BusinessException(ErrorCode.IS_NOT_OWNER);
        }
    }


    @Transactional
    public void buildImage(String lang, String imageName,String version) throws Exception {
        switch(lang) {
            case "gcc": {
                dockerCompileService.GCC(imageName,version);
                break;
            }
            case "python": {
                dockerCompileService.Python(imageName,version);
                break;
            }
        }
    }
}
