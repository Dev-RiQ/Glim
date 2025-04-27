package com.glim.tag.controller;

import com.glim.tag.dto.TagRequest;
import com.glim.tag.service.ViewTagService;
import com.glim.common.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class ViewTagController {

    private final ViewTagService viewTagService;

    // 태그들을 본 걸 저장하는 API
    @PostMapping("/view")
    public ResponseEntity<String> viewTags(@RequestBody List<String> tags) {
        Long userId = SecurityUtil.getCurrentUserId();
        viewTagService.saveAllViews(userId, tags);
        return ResponseEntity.ok("태그 상호작용 저장 완료");
    }

    // 내가 많이 본 태그 최대 20개 조회 API
    @GetMapping("/my")
    public ResponseEntity<List<String>> getMyTopTags() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(viewTagService.getTopTagsByUser(userId));
    }

    @PostMapping("/interaction")
    public ResponseEntity<String> saveTagInteractions(@RequestBody TagRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        viewTagService.saveAllViews(userId, request.getTags());
        return ResponseEntity.ok("태그 상호작용 기록 완료");
    }
}
