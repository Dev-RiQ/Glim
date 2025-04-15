package com.glim.dummy.repository;

import com.glim.dummy.domain.Dummy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DummyRepository extends JpaRepository<Dummy, Long> {
}
