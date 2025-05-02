package com.glim.borad.repository;

import com.glim.borad.domain.BoardViews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardViewRepository extends JpaRepository<BoardViews, Long> {
    List<BoardViews> findByUserIdAndBoardId(Long userId, Long boardId);
    boolean existsByUserIdAndBoardId(Long userId, Long boardId);
}
