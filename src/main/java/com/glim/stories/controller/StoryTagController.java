package com.glim.stories.controller;

import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.stories.dto.request.AddStoryTagRequest;
import com.glim.stories.service.StoryTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/storyTag")
public class StoryTagController {

    private final StoryTagService storyTagService;

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddStoryTagRequest request) {
        storyTagService.insert(request);
        return StatusResponseDTO.ok("스토리 테그하기");
    }

//    @DeleteMapping("/{id}")
//    public StatusResponseDTO delete(@PathVariable Long id) {
//        storyTagService.delete(id);
//        return StatusResponseDTO.ok();
//    }
}
