package com.glim.borad.repository;

import com.glim.borad.domain.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Boards, Long> {
    List<Boards> findAllById(Long id);
}
