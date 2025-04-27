package com.glim.stories.controller;

import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.stories.dto.request.AddStoryShareRequest;
import com.glim.stories.service.StoryShareService;
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
        return StatusResponseDTO.ok("스토리 공유하기");
    }

//    @DeleteMapping("/{id}")
//    public StatusResponseDTO delete(@PathVariable Long id) {
//        storyShareService.delete(id);
//        return StatusResponseDTO.ok();
//    }
}
