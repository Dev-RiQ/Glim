package com.glim.user.repository;

import com.glim.user.domain.Follow;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByFollowerUserIdAndFollowingUserId(Long followerUserId, Long followingUserId);
    boolean existsByFollowerUserIdAndFollowingUserId(Long followerUserId, Long followingUserId);
    List<Follow> findAllByFollowerUserId(Long followerUserId);
    List<Follow> findAllByFollowingUserId(Long followingUserId);
    List<Follow> findTop20ByFollowerUserIdOrderByIdDesc(Long userId);
    List<Follow> findByFollowerUserIdAndIdLessThanOrderByIdDesc(Long userId, Long offset, PageRequest pageRequest);
    List<Follow> findTop20ByFollowingUserIdOrderByIdDesc(Long userId);

    @Query("SELECT f FROM Follow f WHERE f.followingUserId = :userId AND f.id < :offset ORDER BY f.id DESC")
    List<Follow> findByFollowingUserIdAndIdLessThanOrderByIdDesc(
            @Param("userId") Long userId,
            @Param("offset") Long offset,
            Pageable pageable
    );

    void deleteByFollowerUserIdOrFollowingUserId(Long followerUserId, Long followingUserId);



}
