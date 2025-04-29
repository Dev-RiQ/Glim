package com.glim.borad.controller;

import com.glim.borad.dto.request.AddCommentLikeRequest;
import com.glim.borad.service.CommentLikeService;
import com.glim.borad.service.CommentService;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/commentLike")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;
    private final CommentService commentService;

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddCommentLikeRequest request) {
        commentLikeService.insert(request);
        commentService.updateLike(request.getCommentId(), 1);
        return StatusResponseDTO.ok("댓글 좋아요 완료");
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id, @RequestBody AddCommentLikeRequest request) {
        commentLikeService.delete(id);
        commentService.updateLike(request.getCommentId(), -1);
        return StatusResponseDTO.ok("댓글 좋아요 취소 완료");
    }
}
