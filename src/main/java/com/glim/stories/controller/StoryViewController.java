package com.glim.stories.controller;

import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.stories.domain.Stories;
import com.glim.stories.dto.request.AddStoryViewRequest;
import com.glim.stories.service.StoryService;
import com.glim.stories.service.StoryViewService;
import com.glim.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/storyView")
public class StoryViewController {

    private final StoryViewService storyViewService;
    private final StoryService storyService;

    @PostMapping({"/{storyId}"})
    public StatusResponseDTO getStory(@PathVariable Long storyId, @AuthenticationPrincipal SecurityUserDto user) {
        if(storyViewService.insert(new AddStoryViewRequest(storyId, user.getId()))){
            storyService.updateView(storyId, 1);
            return StatusResponseDTO.ok("스토리 조회수 추가");
        }
        return StatusResponseDTO.ok("이미 조회한 스토리 추가");
    }
}
