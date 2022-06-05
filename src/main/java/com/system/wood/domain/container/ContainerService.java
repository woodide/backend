package com.system.wood.domain.container;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ContainerService {

    private final ContainerRepository containerRepository;

    public Container getContainerByName(String containerName) {
        return containerRepository.findContainerByContainerName(containerName);
    }

    public Container getContainerById(Long containerId) {
        return containerRepository.findById(containerId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Id가 %d인 컨테이너 데이터가 존재하지 않습니다.",containerId)));
    }

    @Transactional
    public Long save(Container container) {
        Container savedContainer = containerRepository.save(container);
        return savedContainer.getId();
    }

    @Transactional
    public String removeContainer(Long containerId) {
        Container container = getContainerById(containerId);
        String dockerContainerId = container.getDockerContainerId();
        containerRepository.delete(container);

        return dockerContainerId;
    }
}
