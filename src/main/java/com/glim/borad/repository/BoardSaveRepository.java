package com.glim.borad.repository;

import com.glim.borad.domain.BoardSaves;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BoardSaveRepository extends JpaRepository<BoardSaves, Long> {
    void deleteByBoardIdAndUserId(Long boardId, Long userId);

    List<BoardSaves> findByUserId(Long userId);

    BoardSaves findByBoardIdAndUserId(Long boardId, Long userId);

    List<BoardSaves> findAllByUserIdOrderByIdDesc(Long userId, Limit of);

    List<BoardSaves> findAllByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long offset, Limit of);
}
