package com.glim.stories.controller;

import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.stories.domain.Stories;
import com.glim.stories.dto.request.AddStoryRequest;
import com.glim.stories.dto.response.ViewStoryResponse;
import com.glim.stories.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/story")
public class StoryController {

    private final StoryService storyService;

    @GetMapping({"/{userId}"})
    public StatusResponseDTO getStoryList(@PathVariable Long userId) {
        List<ViewStoryResponse> list = storyService.getStoryList(userId);
        return StatusResponseDTO.ok(list);
    }

    @PostMapping({"", "/"})
    public StatusResponseDTO add(@RequestBody AddStoryRequest request, @AuthenticationPrincipal SecurityUserDto user) {
        request.setUserId(user.getId());
        storyService.insert(request);
        return StatusResponseDTO.ok("스토리 추가 완료");
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id) {
        storyService.delete(id);
        return StatusResponseDTO.ok("스토리 삭제 완료");
    }
}
