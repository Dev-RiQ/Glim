package com.glim.borad.repository;

import com.glim.borad.domain.BoardSaves;
import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.Boards;
import com.glim.borad.domain.Option;
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
    List<Boards> findByCreatedAtBetweenAndBoardTypeAndViewLikesOrderByLikesDescCreatedAtDesc(
            LocalDateTime start, LocalDateTime end, BoardType boardType, Option viewLikes, Pageable pageable);

    List<Boards> findByIdIn(List<Long> boardIdList);

    List<Boards> findAllByUserIdInOrderByIdDesc(List<Long> followedUserIds, Limit of);

    List<Boards> findAllByUserIdInAndIdLessThanOrderByIdDesc(List<Long> followedUserIds, Long offset, Limit of);

    List<Boards> findAllByIdNotInOrderByIdDesc(List<Long> excludeIds, Limit of);

    List<Boards> findAllByIdNotInAndIdLessThanOrderByIdDesc(List<Long> excludeIds, Long offset, Limit of);

    List<Boards> findAllByBoardTypeOrderByIdDesc(BoardType boardType, Limit of);

    List<Boards> findAllByBoardTypeAndIdLessThanOrderByIdDesc(BoardType boardType, Long offset, Limit of);

    Optional<List<Boards>> findAllByUserIdAndBoardTypeOrderByIdDesc(Long id, BoardType boardType, Limit of);

    Optional<List<Boards>> findAllByUserIdAndBoardTypeAndIdLessThanOrderByIdDesc(Long id, BoardType boardType, Long offset, Limit of);

    Optional<List<Boards>> findAllByOrderByIdDesc(Limit of);

    Optional<List<Boards>> findAllByTagUserIdsContainingOrderByIdDesc(String userId, Limit of);

    Optional<List<Boards>> findAllByTagUserIdsContainingAndIdLessThanOrderByIdDesc(String userId, Long offset, Limit of);

    Optional<List<Boards>> findAllByIdLessThanOrderByIdDesc(Long offset, Limit of);

    Optional<List<Boards>> findAllByIdIn(List<Long> saveList);

    Optional<List<Boards>> findAllByUserId(Long userId);

    List<Boards> findAllByBgmId(Long id);

    int countByUserIdAndBoardTypeNot(Long userId, BoardType boardType);

    Optional<List<Boards>> findAllByUserIdAndBoardTypeNotOrderByIdDesc(Long userId, BoardType boardType, Limit of);

    Optional<List<Boards>> findAllByUserIdAndBoardTypeNotAndIdLessThanOrderByIdDesc(Long userId, BoardType boardType, Long offset, Limit of);
}

