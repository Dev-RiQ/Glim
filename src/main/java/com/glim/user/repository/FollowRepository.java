package com.glim.user.repository;

import com.glim.user.domain.Follow;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByFollowerUserIdAndFollowingUserId(Long followerUserId, Long followingUserId);
    boolean existsByFollowerUserIdAndFollowingUserId(Long followerUserId, Long followingUserId);
    List<Follow> findAllByFollowerUserId(Long followerUserId);
    List<Follow> findAllByFollowingUserId(Long followingUserId);
    List<Follow> findTop20ByFollowerUserIdOrderByIdDesc(Long userId);
    List<Follow> findByFollowerUserIdAndIdLessThanOrderByIdDesc(Long userId, Long offset, PageRequest pageRequest);
    List<Follow> findTop20ByFollowingUserIdOrderByIdDesc(Long userId);
    List<Follow> findByFollowingUserIdAndIdLessThanOrderByIdDesc(Long followingUserId, Long offset, Pageable pageable);
    void deleteByFollowerUserIdOrFollowingUserId(Long followerUserId, Long followingUserId);
}
