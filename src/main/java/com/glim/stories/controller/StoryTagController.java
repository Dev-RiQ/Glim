package com.glim.stories.controller;

import com.glim.borad.domain.BoardTags;
import com.glim.borad.dto.request.AddBoardTagRequest;
import com.glim.borad.dto.request.UpdateBoardTagRequest;
import com.glim.borad.service.BoardTagService;
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
        return StatusResponseDTO.ok();
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id) {
        storyTagService.delete(id);
        return StatusResponseDTO.ok();
    }
}
