package com.glim.stories.controller;

import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.stories.dto.request.AddStoryShareRequest;
import com.glim.stories.dto.request.AddStoryTagRequest;
import com.glim.stories.service.StoryShareService;
import com.glim.stories.service.StoryTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/storyShare")
public class StoryShareController {

    private final StoryShareService storyShareService;

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddStoryShareRequest request) {
        storyShareService.insert(request);
        return StatusResponseDTO.ok();
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id) {
        storyShareService.delete(id);
        return StatusResponseDTO.ok();
    }
}
