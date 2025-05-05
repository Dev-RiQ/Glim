package com.glim.borad.repository;

import com.glim.borad.domain.BoardSaves;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BoardSaveRepository extends JpaRepository<BoardSaves, Long> {
    void deleteByBoardIdAndUserId(Long boardId, Long userId);

    List<BoardSaves> findByUserId(Long userId);

    BoardSaves findByBoardIdAndUserId(Long boardId, Long userId);

    Optional<List<BoardSaves>> findAllByUserIdOrderByIdDesc(Long userId, Limit of);

    Optional<List<BoardSaves>> findAllByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long offset, Limit of);

    boolean existsByBoardIdAndUserId(Long id, Long id1);

    void deleteAllByBoardId(Long boardId);

    void deleteAllByUserId(Long userId);
}
