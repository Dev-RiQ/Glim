package com.glim.borad.controller;

import com.glim.borad.dto.request.AddCommentsRequest;
import com.glim.borad.dto.request.UpdateCommentsRequest;
import com.glim.borad.dto.response.ViewCommentsResponse;
import com.glim.borad.dto.response.ViewReplyCommentResponse;
import com.glim.borad.service.BoardService;
import com.glim.borad.service.CommentService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentsController {

    private final CommentService commentService;
    private final BoardService boardService;

    @GetMapping({"/{boardId}","/{boardId}/{offset}"})
    public StatusResponseDTO list(@PathVariable Long boardId, @PathVariable(required = false) Long offset) {
        List<ViewCommentsResponse> board = commentService.list(boardId, offset);
        return StatusResponseDTO.ok(board);
    }

    @GetMapping({"/reply/{replyCommentId}","/reply/{replyCommentId}/{offset}"})
    public StatusResponseDTO replyList(@AuthenticationPrincipal SecurityUserDto user, @PathVariable Long replyCommentId, @PathVariable(required = false) Long offset) {
        List<ViewReplyCommentResponse> board = commentService.replyList(replyCommentId, offset, user);
        return StatusResponseDTO.ok(board);
    }

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddCommentsRequest request, @AuthenticationPrincipal SecurityUserDto user) {
        commentService.insert(request, user.getId());
        boardService.updateComment(request.getBoardId(), 1);
        return StatusResponseDTO.ok("댓글 추가 완료");
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id) {
        commentService.delete(id);
        return StatusResponseDTO.ok("댓글 삭제 완료");
    }
}
