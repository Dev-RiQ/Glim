package com.glim.user.controller;

import com.glim.notification.service.NotificationService;
import com.glim.user.dto.request.FollowRequest;
import com.glim.user.dto.response.FollowRecommendResponse;
import com.glim.user.dto.response.FollowResponse;
import com.glim.user.dto.response.FollowUserResponse;
import com.glim.user.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
public class FollowController {

    private final FollowService followService;
    private final NotificationService notificationService;

    /**
     * ➕ 팔로우 요청
     * POST /api/v1/follow
     * @param request (followingId만 포함)
     * @return 팔로우 성공 메시지
     */
    @PostMapping
    public ResponseEntity<FollowResponse> follow(@RequestBody FollowRequest request) {
        followService.follow(request.getFollowingId()); // followerId는 내부에서 처리
//        notificationService.send(request.getFollowingId())
        return ResponseEntity.ok(new FollowResponse("팔로우 성공", true));
    }

    /**
     * ➖ 언팔로우 요청
     * DELETE /api/v1/follow
     * @param request (followingId만 포함)
     * @return 언팔로우 성공 메시지
     */
    @DeleteMapping
    public ResponseEntity<FollowResponse> unfollow(@RequestBody FollowRequest request) {
        followService.unfollow(request.getFollowingId());
        return ResponseEntity.ok(new FollowResponse("언팔로우 성공", true));
    }

    /**
     * ❓ 팔로우 여부 확인
     * GET /api/v1/follow/check?followingId=5
     * @return true or false
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> isFollowing(@RequestParam Long followingId) {
        return ResponseEntity.ok(followService.isFollowing(followingId));
    }

    /**
     * 📄 내가 팔로우한 유저 목록
     * GET /api/v1/follow/followings/{userId}
     */
    @GetMapping("/followings/{userId}")
    public ResponseEntity<List<FollowUserResponse>> getFollowings(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowings(userId));
    }

    /**
     * 📄 나를 팔로우한 유저 목록
     * GET /api/v1/follow/followers/{userId}
     */
    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<FollowUserResponse>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    /**
     * 💡 맞팔 기반 추천
     * GET /api/v1/follow/recommend
     */
    @GetMapping("/recommend")
    public ResponseEntity<List<FollowRecommendResponse>> getRecommendedUsers() {
        return ResponseEntity.ok(followService.getRecommendedUsers());
    }
}
