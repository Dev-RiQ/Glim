package com.glim.user.service;

import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.security.util.SecurityUtil;
import com.glim.notification.domain.Type;
import com.glim.notification.service.NotificationService;
import com.glim.stories.service.StoryService;
import com.glim.user.domain.Follow;
import com.glim.user.domain.User;
import com.glim.user.dto.response.FollowRecommendResponse;
import com.glim.user.dto.response.FollowUserResponse;
import com.glim.user.repository.FollowRepository;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)

public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final StoryService storyService;
    private final AwsS3Util awsS3Util;

    public FollowService(
            FollowRepository followRepository,
            UserRepository userRepository,
            @Lazy NotificationService notificationService,
            StoryService storyService,
            AwsS3Util awsS3Util) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.storyService = storyService;
        this.awsS3Util = awsS3Util;
    }

    // ✅ 로그인한 사용자 기준 팔로우
    @Transactional
    public void follow(Long followingId, SecurityUserDto user) {
        Long followerId = SecurityUtil.getCurrentUserId();

        // 자기 자신 팔로우 할시
        if (followerId.equals(followingId))
            throw new CustomException(ErrorCode.DUPLICATE_FOLLOW);

        // 이미 팔로우 된 user
        if (followRepository.existsByFollowerUserIdAndFollowingUserId(followerId, followingId))
            throw new CustomException(ErrorCode.DUPLICATE_FOLLOW);

        followRepository.save(Follow.builder()
                .followerUserId(followerId)
                .followingUserId(followingId)
                .build());

        User fromUser = userRepository.findById(followerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User toUser = userRepository.findById(followingId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        fromUser .setFollowings(fromUser.getFollowings() + 1);
        toUser .setFollowers(toUser .getFollowers() + 1);

        // 알림 보내기
        notificationService.send(toUser.getId(), Type.FOLLOW, fromUser.getId(), user);

    }

    // ✅ 로그인한 사용자 기준 언팔
    @Transactional
    public void unfollow(Long followingId) {
        Long followerId = SecurityUtil.getCurrentUserId();

        if (followerId.equals(followingId)) {
            throw new CustomException(ErrorCode.SELF_UNFOLLOW_NOT_ALLOWED);
        }
        boolean isFollowing = followRepository.existsByFollowerUserIdAndFollowingUserId(followerId, followingId);
        if (!isFollowing) {
            throw new CustomException(ErrorCode.NOT_FOLLOWING_YET);
        }

        followRepository.deleteByFollowerUserIdAndFollowingUserId(followerId, followingId);

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        follower.setFollowings(Math.max(0, follower.getFollowings() - 1));
        following.setFollowers(Math.max(0, following.getFollowers() - 1));

        userRepository.save(follower);
        userRepository.save(following);
    }

    // ✅ 로그인한 사용자가 특정 유저를 팔로우했는지
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerUserIdAndFollowingUserId(followerId, followingId);
    }


    // 특정 사용자가 팔로잉한 유저 목록
    public List<FollowUserResponse> getFollowings(Long userId, Long offset) {
        List<Follow> followList = (offset == null)
                ? followRepository.findTop20ByFollowerUserIdOrderByIdDesc(userId)
                : followRepository.findByFollowerUserIdAndIdLessThanOrderByIdDesc(userId, offset, PageRequest.of(0, 30));

        return followList.stream()
                .map(f -> {
                    User user = userRepository.findById(f.getFollowingUserId())
                            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                    user.setImg(awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128));
                    boolean isStory = storyService.isStory(user.getId());
                    return FollowUserResponse.from(user, isStory);
                })
                .toList();
    }

    // 특정 사용자를 팔로우한 유저 목록
    public List<FollowUserResponse> getFollowers(Long userId, Long offset) {
        List<Follow> followList = (offset == null)
                ? followRepository.findTop20ByFollowingUserIdOrderByIdDesc(userId)
                : followRepository.findByFollowingUserIdAndIdLessThanOrderByIdDesc(userId, offset, PageRequest.of(0, 20));

        return followList.stream()
                .map(f -> {
                    User user = userRepository.findById(f.getFollowerUserId())
                            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                    user.setImg(awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128));
                    boolean isStory = storyService.isStory(user.getId());
                    return FollowUserResponse.from(user, isStory);
                })
                .toList();
    }


    // ✅ 맞팔로우 기반 추천
    public List<FollowRecommendResponse> getRecommendedUsers() {
        Long myId = SecurityUtil.getCurrentUserId();

        // 내가 팔로우한 유저 추출
        Set<Long> myFollowings = followRepository.findAllByFollowerUserId(myId).stream()
                .map(Follow::getFollowingUserId).collect(toSet());
        // 나를 팔로우한 유저 추출
        Set<Long> myFollowers = followRepository.findAllByFollowingUserId(myId).stream()
                .map(Follow::getFollowerUserId).collect(toSet());
        // 맞팔 유저 추출 ( 위 두개의 교집합 )
        Set<Long> mutuals = myFollowings.stream()
                .filter(myFollowers::contains).collect(toSet());

        // 맞팔 유저들이 팔로우 중인 사람들 중 내가 아직 팔로우하지 않은 유저 추천 최대 5개
        Set<Long> candidateIds = new HashSet<>();
        for (Long mutualId : mutuals) {
            followRepository.findAllByFollowerUserId(mutualId).forEach(f -> {
                Long targetId = f.getFollowingUserId();
                if (!targetId.equals(myId) && !myFollowings.contains(targetId)) {
                    candidateIds.add(targetId);
                }
            });
        }

        return userRepository.findAllById(candidateIds).stream()
                .sorted(Comparator.comparing(User::getFollowers).reversed())
                .limit(5)
                .map(u -> new FollowRecommendResponse(
                        u.getId(),
                        u.getNickname(),
                        awsS3Util.getURL(u.getImg(), FileSize.IMAGE_128),  // ✅ 여기에서 가공
                        u.getName(),
                        storyService.isStory(u.getId())
                ))
                .collect(toList());
    }

    @Transactional
    public void deleteFollowByUser(Long userId) {
        followRepository.deleteByFollowerUserIdOrFollowingUserId(userId, userId);
    }

    public List<FollowUserResponse> getHasStoryList() {
        Long id = SecurityUtil.getCurrentUserId();
        List<Follow> list = followRepository.findAllByFollowerUserId(id);
        if(list.isEmpty()) return Collections.emptyList();
        List<FollowUserResponse> responses = new ArrayList<>();
        for(Follow follow : list) {
            if(storyService.isStory(follow.getFollowingUserId())){
                User user = userRepository.findById(follow.getFollowingUserId())
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                if(user == null) continue;
                user.setImg(awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128));
                responses.add(FollowUserResponse.from(user, true));
            }
        }
        return responses;
    }
}
