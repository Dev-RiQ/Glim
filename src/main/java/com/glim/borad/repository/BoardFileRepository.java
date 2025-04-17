package com.glim.borad.repository;

import com.glim.borad.domain.BoardFiles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFiles, Long> {
}
