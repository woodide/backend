package com.system.wood.domain.container;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ContainerService {

    private final ContainerRepository containerRepository;

    public Container getContainerById(Long containerId) {
        return containerRepository.findById(containerId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Id가 %d인 컨테이너 데이터가 존재하지 않습니다.",containerId)));
    }

    public Optional<Container> getContainerByName(String containerName) {
        return containerRepository.findByContainerName(containerName);
    }

    public Container getContainer(String containerName) {
        return containerRepository.findByContainerName(containerName).orElseThrow(() ->
                new EntityNotFoundException(String.format("컨테이너 이름이 %s인 컨테이너 데이터가 존재하지 않습니다.",containerName)));
    }

    @Transactional
    public Container save(Container container) {
        Container savedContainer = containerRepository.save(container);
        return savedContainer;
    }

    @Transactional
    public String removeContainer(Long containerId) {
        Container container = getContainerById(containerId);
        String dockerContainerId = container.getDockerContainerId();
        containerRepository.delete(container);

        return dockerContainerId;
    }

    @Transactional
    public void updateCount(Container container) {
        container.addCountByOne();
    }
}
