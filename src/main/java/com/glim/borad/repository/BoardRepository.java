package com.glim.borad.repository;

import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.Boards;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Boards, Long> {

    @Override
    Optional<Boards> findById(Long aLong);

    void deleteByUserId(Long userId);

    int countByUserId(Long userId);

    // ✅ 조회 기간 + 타입별 조회수 높은 게시글 Top 20 조회
    List<Boards> findByCreatedAtBetweenAndBoardTypeOrderByViewsDescCreatedAtDesc(LocalDateTime start, LocalDateTime end, BoardType type, Pageable pageable);

    // ✅ 조회 기간 + 타입별 좋아요 높은 게시글 Top 20 조회
    List<Boards> findByCreatedAtBetweenAndBoardTypeOrderByLikesDescCreatedAtDesc(LocalDateTime start, LocalDateTime end, BoardType type, Pageable pageable);

    List<Boards> findByIdIn(List<Long> boardIdList);

    List<Boards> findAllByUserIdOrderByIdDesc(Long userId, Limit of);

    List<Boards> findAllByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long offset, Limit of);

    List<Boards> boardType(BoardType boardType);

    List<Boards> findAllByUserIdInOrderByIdDesc(List<Long> followedUserIds, Limit of);

    List<Boards> findAllByUserIdInAndIdLessThanOrderByIdDesc(List<Long> followedUserIds, Long offset, Limit of);

    List<Boards> findAllByIdNotInOrderByIdDesc(List<Long> excludeIds, Limit of);

    List<Boards> findAllByIdNotInAndIdLessThanOrderByIdDesc(List<Long> excludeIds, Long offset, Limit of);

    List<Boards> findAllByUserId(Long userId);
}

