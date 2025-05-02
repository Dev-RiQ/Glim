package com.glim.tag.controller;


import com.glim.common.security.util.SecurityUtil;
import com.glim.tag.service.ViewTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class ViewTagController {

    private final ViewTagService viewTagService;

    @PostMapping("/view")
    public ResponseEntity<String> recordTags(@RequestBody List<String> tags) {
        Long userId = SecurityUtil.getCurrentUserId();

        viewTagService.saveAllViews(userId, tags);

        return ResponseEntity.ok("태그 상호작용 저장 완료");
    }
}
