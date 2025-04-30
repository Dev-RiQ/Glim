package com.glim.borad.repository;

import com.glim.borad.domain.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLikes, Long> {
    void deleteByBoardIdAndUserId(Long boardId, Long userId);

    Boolean existsByBoardIdAndUserId(Long boardId, Long userId);
}
