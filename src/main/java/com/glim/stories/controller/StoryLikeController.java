package com.glim.stories.controller;

import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.stories.domain.Stories;
import com.glim.stories.dto.request.AddStoryLikeRequest;
import com.glim.stories.service.StoryLikeService;
import com.glim.stories.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        Stories stories = storyService.updateLike(request.getStoryId(), 1);
        return StatusResponseDTO.ok(stories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@RequestBody AddStoryLikeRequest request, @PathVariable Long id) {
        storyLikeService.delete(id);
        storyService.updateLike(request.getStoryId(), -1);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
