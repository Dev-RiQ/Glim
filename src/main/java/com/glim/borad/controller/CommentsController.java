package com.glim.borad.controller;

import com.glim.borad.dto.request.AddCommentsRequest;
import com.glim.borad.dto.request.UpdateCommentsRequest;
import com.glim.borad.dto.response.ViewCommentsResponse;
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
    public StatusResponseDTO replyList(@PathVariable Long replyCommentId, @PathVariable(required = false) Long offset) {
        List<ViewCommentsResponse> board = commentService.replyList(replyCommentId, offset);
        return StatusResponseDTO.ok(board);
    }

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddCommentsRequest request) {
        commentService.insert(request);
        boardService.updateComment(request.getBoardId(), 1);
        return StatusResponseDTO.ok("댓글 추가 완료");
    }

    @PutMapping("/{id}")
    public StatusResponseDTO update(@PathVariable Long id, @RequestBody UpdateCommentsRequest request) {
        commentService.update(id, request);
        return StatusResponseDTO.ok("댓글 수정 완료");
    }

    @DeleteMapping("/{boardId}/{id}")
    public StatusResponseDTO delete(@PathVariable Long boardId, @AuthenticationPrincipal SecurityUserDto user) {
        commentService.delete(boardId, user.getId());
        return StatusResponseDTO.ok("댓글 삭제 완료");
    }
}
