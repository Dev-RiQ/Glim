package com.glim.borad.repository;

import com.glim.borad.domain.BoardSaves;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardSaveRepository extends JpaRepository<BoardSaves, Long> {
}
