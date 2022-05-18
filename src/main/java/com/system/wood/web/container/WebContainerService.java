package com.system.wood.web.container;

import com.system.wood.domain.member.Member;
import com.system.wood.domain.container.Container;
import com.system.wood.domain.container.ContainerService;
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
        // db에서 container 정보 삭제
        String dockerContainerId = containerService.removeContainer(containerId);

        // docker container stop & remove
        infraService.deleteContainer(dockerContainerId);

        // todo: 기존에 저장한 폴더 삭제
    }
}
