package com.glim.user.controller;

import com.glim.common.security.util.SecurityUtil;
import com.glim.common.statusResponse.StatusResponseDTO;
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
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;
    /**
     * ➕ 팔로우 요청
     * POST /api/v1/follow
     * @param request (followingId만 포함)
     * @return 팔로우 성공 메시지
     */
    @PostMapping
    public ResponseEntity<FollowResponse> follow(@RequestBody FollowRequest request) {
        followService.follow(request.getFollowingId()); // followerId는 내부에서 처리
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
    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> checkFollowing(@PathVariable("userId") Long followingId) {
        Long myId = SecurityUtil.getCurrentUserId();  // 🔐 현재 로그인한 유저 ID
        boolean isFollowing = followService.isFollowing(myId, followingId); // ✅ 팔로우 여부 확인
        return ResponseEntity.ok(isFollowing);  // ✅ true 또는 false 응답
    }

    /**
     * 📄 내가 팔로우한 유저 목록
     * GET /api/v1/follow/followings/{userId}
     */
    @GetMapping({"/followings/{userId}","/followings/{userId}/{offset}"})
    public StatusResponseDTO getFollowings(@PathVariable Long userId,
                                           @PathVariable(required = false) Long offset) {
        List<FollowUserResponse> followings = followService.getFollowings(userId, offset);
        return StatusResponseDTO.ok(followings);
    }

    /**
     * 📄 나를 팔로우한 유저 목록
     * GET /api/v1/follow/followers/{userId}
     */
    @GetMapping({"/followers/{userId}", "/followers/{userId}/{offset}"})
    public StatusResponseDTO getFollowers(
            @PathVariable Long userId,
            @PathVariable(required = false) Long offset) {

        List<FollowUserResponse> followers = followService.getFollowers(userId, offset);
        return StatusResponseDTO.ok(followers);
    }


    /**
     * 💡 맞팔 기반 추천
     * GET /api/v1/follow/recommend
     */
    @GetMapping("/recommend")
    public ResponseEntity<List<FollowRecommendResponse>> getRecommendedUsers() {
        return ResponseEntity.ok(followService.getRecommendedUsers());
    }

    @GetMapping("/story")
    public StatusResponseDTO getStory() {
        return StatusResponseDTO.ok(followService.getHasStoryList()); 
    }
}
