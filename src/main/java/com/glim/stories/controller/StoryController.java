package com.glim.stories.controller;

import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.stories.dto.request.AddStoryRequest;
import com.glim.stories.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/story")
public class StoryController {

    private final StoryService storyService;

    @PostMapping({"", "/"})
    public StatusResponseDTO add(@RequestBody AddStoryRequest request) {
        storyService.insert(request);
        return StatusResponseDTO.ok("스토리 추가 완료");
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id) {
        storyService.delete(id);
        return StatusResponseDTO.ok("스토리 삭제 완료");
    }
}
