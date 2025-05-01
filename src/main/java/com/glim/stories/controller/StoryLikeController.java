package com.glim.stories.controller;

import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.stories.dto.request.AddStoryLikeRequest;
import com.glim.stories.service.StoryLikeService;
import com.glim.stories.service.StoryService;
import com.glim.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/storyLike")
public class StoryLikeController {

    private final StoryService storyService;
    private final StoryLikeService storyLikeService;

    @PostMapping({"/{storyId}"})
    public StatusResponseDTO add(@PathVariable Long storyId, @AuthenticationPrincipal SecurityUserDto user) {
        storyLikeService.insert(new AddStoryLikeRequest(storyId, user.getId()));
        storyService.updateLike(storyId, 1);
        return StatusResponseDTO.ok("스토리 좋아요");
    }

    @DeleteMapping("/{storyId}")
    public StatusResponseDTO delete(@PathVariable Long storyId, @AuthenticationPrincipal SecurityUserDto user) {
        storyLikeService.delete(storyId, user.getId());
        storyService.updateLike(storyId, -1);
        return StatusResponseDTO.ok("스토리 좋아요 취소");
    }
}
