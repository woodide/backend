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

    public Container getContainerById(Long containerId) {
        return containerRepository.findById(containerId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Id가 %d인 컨테이너 데이터가 존재하지 않습니다.",containerId)));
    }

    // todo: 안 쓰는 포트넘버를 구하는 로직 추가
    public Integer getAvailablePortNum() {
        // 서버 켜지면 테이블에 있는 포트 다  읽어서 변수 저장

        // 이때부터는 그냥 변수만 사용
        return 8443;
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
