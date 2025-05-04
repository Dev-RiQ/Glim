package com.glim.borad.repository;

import com.glim.borad.domain.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLikes, Long> {

    Boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    BoardLikes findByBoardIdAndUserId(Long boardId, Long userId);

    void deleteByUserId(Long boardId);
}
