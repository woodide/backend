package com.system.wood.web.container.service;

import com.system.wood.domain.member.Member;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.container.ContainerService;
import com.system.wood.global.error.BusinessException;
import com.system.wood.global.error.ErrorCode;
<<<<<<< HEAD:src/main/java/com/system/wood/web/container/service/WebContainerService.java
import com.system.wood.infra.dockercontainer.DockerContainerService;
=======
import com.system.wood.infra.DockerCompileService;
import com.system.wood.infra.InfraService;
>>>>>>> e8f21e559ae94ca08171988ed533609be3f53a56:src/main/java/com/system/wood/web/container/WebContainerService.java
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
    public void createContainer(String containerName, Member member) throws IOException {
        Container newContainer = infraService.createContainer(containerName, member);

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

    public void validateContainerOwner(Member member, Long containerId) throws BusinessException{
        if (!member.equals(containerService.getContainerById(containerId).getMember())) {
            log.info(String.format("error: member(id:%d)는 컨테이너(id:%d)를 소유하지 않습니다.", member.getId(), containerId));
            throw new BusinessException(ErrorCode.IS_NOT_OWNER);
        }
    }


    @Transactional
    public void buildImage(String lang, String imageName,String version) throws IOException {
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
