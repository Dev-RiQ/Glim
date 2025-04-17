package com.glim.borad.controller;

import com.glim.borad.dto.request.AddCommentsRequest;
import com.glim.borad.dto.request.UpdateCommentsRequest;
import com.glim.borad.dto.response.ViewCommentsResponse;
import com.glim.borad.service.BoardService;
import com.glim.borad.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentsController {

    private final CommentService commentService;
    private final BoardService boardService;

    @GetMapping({"","/{id}"})
    public List<ViewCommentsResponse> list() {
        List<ViewCommentsResponse> board = commentService.list();
        return ResponseEntity.ok(board).getBody();
    }

    @PostMapping({"","/"})
    public ResponseEntity<HttpStatus> add(@RequestBody AddCommentsRequest request) {
        commentService.insert(request);
        boardService.updateComment(request.getBoardId(), 1);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody UpdateCommentsRequest request) {
        commentService.update(id, request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@RequestBody AddCommentsRequest request, @PathVariable Long id) {
        commentService.delete(id);
        boardService.updateComment(request.getBoardId(), -1);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
