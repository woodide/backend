package com.system.wood.web.container;

import com.system.wood.domain.member.Member;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.container.ContainerService;
import com.system.wood.global.error.BusinessException;
import com.system.wood.global.error.ErrorCode;
import com.system.wood.infra.InfraService;
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
    private final InfraService infraService;

    @Transactional
    public void createContainer(String containerName, Member member) throws IOException {
        Container newContainer = infraService.createContainer(containerName, member);
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
}
