package com.glim.user.repository;

import com.glim.user.domain.Dummy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DummyRepository extends JpaRepository<Dummy, Long> {
}
