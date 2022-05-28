package com.system.wood.domain.signup;

import com.system.wood.domain.signup.SignupWaiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignupWaitingRepository extends JpaRepository<SignupWaiting, Long> {
    Optional<SignupWaiting> findById(Long id);
    Optional<SignupWaiting> findByEmail(String email);
}
