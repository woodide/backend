package com.system.wood.domain.container;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContainerRepositoryTest {

    @Autowired
    private ContainerRepository containerRepository;

    @Test
    public void 모든_포트번호를_얻는다() {
        List<Integer> allPorts = containerRepository.getAllPorts();
        for (Integer allPort : allPorts) {
            System.out.println("allPort = " + allPort);
        }
    }
}