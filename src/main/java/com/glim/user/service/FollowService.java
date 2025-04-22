package com.glim.user.service;


import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.user.domain.Follow;
import com.glim.user.domain.User;
import com.glim.user.dto.response.FollowUserResponse;
import com.glim.user.repository.FollowRepository;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public void follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) throw new IllegalArgumentException("자기 자신 팔로우 불가");
        if (followRepository.existsByFollowerUserIdAndFollowingUserId(followerId, followingId))
            throw new CustomException(ErrorCode.DUPLICATE_FOLLOW);;

        followRepository.save(Follow.builder()
                .followerUserId(followerId)
                .followingUserId(followingId)
                .build());

        User follower = userRepository.findById(followerId).orElseThrow();
        User following = userRepository.findById(followingId).orElseThrow();
        follower.setFollowings(follower.getFollowings() + 1);
        following.setFollowers(following.getFollowers() + 1);
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        followRepository.deleteByFollowerUserIdAndFollowingUserId(followerId, followingId);
        User follower = userRepository.findById(followerId).orElseThrow();
        User following = userRepository.findById(followingId).orElseThrow();
        follower.setFollowings(Math.max(0, follower.getFollowings() - 1));
        following.setFollowers(Math.max(0, following.getFollowers() - 1));
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerUserIdAndFollowingUserId(followerId, followingId);
    }

    public List<FollowUserResponse> getFollowings(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return followRepository.findAllByFollowerUserId(userId).stream()
                .map(f -> userRepository.findById(f.getFollowingUserId())
                        .map(u -> new FollowUserResponse(u.getNickname(), u.getImg()))
                        .orElse(null))
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    public List<FollowUserResponse> getFollowers(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return followRepository.findAllByFollowingUserId(userId).stream()
                .map(f -> userRepository.findById(f.getFollowerUserId())
                        .map(u -> new FollowUserResponse(u.getNickname(), u.getImg()))
                        .orElse(null))
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }
}
