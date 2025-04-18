package com.glim.stories.controller;

import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.stories.domain.Stories;
import com.glim.stories.dto.request.AddStoryViewRequest;
import com.glim.stories.service.StoryService;
import com.glim.stories.service.StoryViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/storyView")
public class StoryViewController {

    private final StoryViewService storyViewService;
    private final StoryService storyService;

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddStoryViewRequest request) {
        storyViewService.insert(request);
        Stories stories = storyService.updateView(request.getStoryId(), 1);
        return StatusResponseDTO.ok();
    }
}
