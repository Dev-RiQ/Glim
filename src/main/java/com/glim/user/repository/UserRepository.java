package com.glim.user.repository;

import com.glim.user.domain.Dummy;
import com.glim.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Dummy, Long> {
    Optional<User> findByUsername(String username);
}
