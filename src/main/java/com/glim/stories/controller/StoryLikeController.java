package com.glim.stories.controller;

import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.stories.dto.request.AddStoryLikeRequest;
import com.glim.stories.service.StoryLikeService;
import com.glim.stories.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/storyLike")
public class StoryLikeController {

    private final StoryService storyService;
    private final StoryLikeService storyLikeService;

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddStoryLikeRequest request) {
        storyLikeService.insert(request);
        storyService.updateLike(request.getStoryId(), 1);
        return StatusResponseDTO.ok("스토리 좋아요");
    }

    @DeleteMapping("/{storyId}/{id}")
    public StatusResponseDTO delete(@PathVariable Long storyId, @PathVariable Long id) {
        storyLikeService.delete(id);
        storyService.updateLike(storyId, -1);
        return StatusResponseDTO.ok("스토리 좋아요 취소");
    }
}
