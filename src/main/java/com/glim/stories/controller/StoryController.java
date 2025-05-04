package com.glim.stories.controller;

import com.glim.borad.dto.response.ViewMyPageBoardResponse;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.stories.domain.Stories;
import com.glim.stories.dto.request.AddStoryRequest;
import com.glim.stories.dto.response.ViewMyPageStoryResponse;
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
    public StatusResponseDTO getStoryList(@PathVariable Long userId, @AuthenticationPrincipal SecurityUserDto user) {
        List<ViewStoryResponse> list = storyService.getStoryList(userId,user.getId());
        return StatusResponseDTO.ok(list);
    }

    @GetMapping({"/view/{storyId}"})
    public StatusResponseDTO getStory(@PathVariable Long storyId, @AuthenticationPrincipal SecurityUserDto user) {
        ViewStoryResponse list = storyService.getStory(storyId, user.getId());
        return StatusResponseDTO.ok(list);
    }


    @GetMapping({"/my","/my/{offset}"})
    public StatusResponseDTO getMyStoryList(@PathVariable(required = false) Long offset, @AuthenticationPrincipal SecurityUserDto user){
        List<ViewMyPageStoryResponse> myStoryList = storyService.myPageStoryList(user.getId(),offset);
        return StatusResponseDTO.ok(myStoryList);
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
