package com.glim.borad.repository;

import com.glim.borad.domain.BoardComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentsRepository extends JpaRepository<BoardComments, Long> {
    List<BoardComments> findAllById(Long id);
}
