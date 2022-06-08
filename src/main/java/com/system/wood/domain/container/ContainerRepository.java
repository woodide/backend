package com.system.wood.domain.container;

import com.system.wood.domain.assigment.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContainerRepository extends JpaRepository<Container, Long> {
    Optional<Container> findByContainerName(String containerName);

    @Query("select c from Container c join fetch c.assignment join fetch c.student where c.containerName = ?1")
    Optional<Container> getContainerByContainerName(String containerName);
}
