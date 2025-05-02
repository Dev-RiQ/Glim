package com.glim.borad.controller;

import com.glim.borad.dto.request.AddCommentLikeRequest;
import com.glim.borad.service.CommentLikeService;
import com.glim.borad.service.CommentService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/commentLike")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;
    private final CommentService commentService;

    @PostMapping({"/{commentId}"})
    public StatusResponseDTO add(@PathVariable Long commentId, @AuthenticationPrincipal SecurityUserDto user) {
        if(commentLikeService.insert(new AddCommentLikeRequest(commentId, user.getId()))){
            commentService.updateLike(commentId, 1);
        }
        return StatusResponseDTO.ok("댓글 좋아요 완료");
    }

    @DeleteMapping("/{commentId}")
    public StatusResponseDTO delete(@PathVariable Long commentId, @AuthenticationPrincipal SecurityUserDto user) {
        if(commentLikeService.delete(commentId, user.getId())){
            commentService.updateLike(commentId, -1);
        }
        return StatusResponseDTO.ok("댓글 좋아요 취소 완료");
    }
}
