package com.system.wood.web;

import com.system.wood.domain.Member;
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
public class DockerService {
    private final ContainerService containerService;
    private final InfraService infraService;

    @Transactional
    public void createContainer(String containerName, Member member) throws IOException {
        Container newContainer = infraService.createContainer(containerName, member);
        containerService.save(newContainer);
    }
}
