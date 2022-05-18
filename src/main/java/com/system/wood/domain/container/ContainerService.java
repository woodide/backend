package com.system.wood.domain.container;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ContainerService {

    private final ContainerRepository containerRepository;
    private TreeSet<Integer> usedPortSet = new TreeSet<>();
    private List<Integer> notUsedPortList = new LinkedList<>();
    private Integer minAvailablePort = 65535;

    // 빈 생성 이후 한 번만 호출된다.
    @PostConstruct
    public void initialize() {
        usedPortSet.addAll(containerRepository.getAllPorts());
    }

    public Container getContainerById(Long containerId) {
        return containerRepository.findById(containerId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Id가 %d인 컨테이너 데이터가 존재하지 않습니다.",containerId)));
    }

    public Integer getAvailablePortNum() {
        return (notUsedPortList.isEmpty())
                ? (usedPortSet.last() + 1)
                : notUsedPortList.remove(0);
    }

    @Transactional
    public Long save(Container container) {
        Container savedContainer = containerRepository.save(container);

        return savedContainer.getId();
    }

    @Transactional
    public String removeContainer(Long containerId) {
        Container container = getContainerById(containerId);
        Integer portNum = container.getPortNum();
        String dockerContainerId = container.getDockerContainerId();
        containerRepository.delete(container);

        return dockerContainerId;
    }
}
