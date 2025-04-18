package com.glim.borad.repository;

import com.glim.borad.domain.Bgms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BgmRepository  extends JpaRepository<Bgms, Long> {
    List<Bgms> findAllById(Long id);
}
