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
    @Query("""
            SELECT b FROM Boards b 
            WHERE b.createdAt BETWEEN :start AND :end
              AND b.boardType = :type
            ORDER BY b.views DESC, b.createdAt DESC
            """)
    List<Boards> findTopBoardsByPeriodAndType(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("type") BoardType type,   // ❗ String -> BoardType (Enum)으로 수정
            Pageable pageable
    );

    // ✅ 조회 기간 + 타입별 좋아요 높은 게시글 Top 20 조회
    @Query("""
            SELECT b FROM Boards b 
            WHERE b.createdAt BETWEEN :start AND :end
              AND b.boardType = :type
            ORDER BY b.likes DESC, b.createdAt DESC
            """)
    List<Boards> findTopBoardsByLikesAndType(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("type") BoardType type,   // ❗ String -> BoardType (Enum)으로 수정
            Pageable pageable
    );

    List<Boards> findByIdIn(List<Long> boardIdList);

    List<Boards> findAllByUserIdOrderByIdDesc(Long userId, Limit of);

    List<Boards> findAllByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long offset, Limit of);
    
    List<Boards> boardType(BoardType boardType);
}

