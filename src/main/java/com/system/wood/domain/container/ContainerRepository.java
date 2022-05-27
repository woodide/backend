package com.system.wood.domain.container;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContainerRepository extends JpaRepository<Container, Long> {

    @Query("select c.portNum from Container c")
    List<Integer> getAllPorts();
}
