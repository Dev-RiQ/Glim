package com.glim.user.controller;

import com.glim.user.dto.request.FollowRequest;
import com.glim.user.dto.response.FollowResponse;
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

    @PostMapping // 팔로우
    public ResponseEntity<FollowResponse> follow(@RequestBody FollowRequest request) {
        followService.follow(request.getFollowerId(), request.getFollowingId());
        return ResponseEntity.ok(new FollowResponse("팔로우 성공", true));
    }

    @DeleteMapping // 언팔로우
    public ResponseEntity<FollowResponse> unfollow(@RequestBody FollowRequest request) {
        followService.unfollow(request.getFollowerId(), request.getFollowingId());
        return ResponseEntity.ok(new FollowResponse("언팔로우 성공", true));
    }

    @GetMapping("/check") // 팔로우 여부 확인
    public ResponseEntity<Boolean> isFollowing(@RequestParam Long followerId, @RequestParam Long followingId) {
        return ResponseEntity.ok(followService.isFollowing(followerId, followingId));
    }

    @GetMapping("/followings/{userId}")
    public ResponseEntity<List<FollowUserResponse>> getFollowings(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowings(userId));
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<FollowUserResponse>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }
}
