package com.system.wood.domain.container;

import io.swagger.models.auth.In;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class ContainerServiceTest {

    @Autowired
    private ContainerRepository containerRepository;
    @Autowired
    private ContainerService containerService;

    @Test
    @DisplayName("사용 중인 포트들을 나열했을 때 공백이 없다면, 사용 중인 포트 중 최대값+1을 반환한다.")
    public void nextPort() {
        Integer availablePortNum = containerService.getAvailablePortNum();
        Integer max = Collections.max(containerRepository.getAllPorts())+1;

        Assertions.assertThat(availablePortNum).isEqualTo(max);
    }
}