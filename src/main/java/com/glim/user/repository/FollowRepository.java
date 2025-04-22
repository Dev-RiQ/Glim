package com.glim.user.repository;

import com.glim.user.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerUserIdAndFollowingUserId(Long followerUserId, Long followingUserId);
    void deleteByFollowerUserIdAndFollowingUserId(Long followerUserId, Long followingUserId);
    boolean existsByFollowerUserIdAndFollowingUserId(Long followerUserId, Long followingUserId);
    List<Follow> findAllByFollowerUserId(Long followerUserId);
    List<Follow> findAllByFollowingUserId(Long followingUserId);
}
