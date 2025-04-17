package com.glim.borad.controller;

import com.glim.borad.dto.request.AddCommentLikeRequest;
import com.glim.borad.service.CommentLikeService;
import com.glim.borad.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/commentLike")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;
    private final CommentService commentService;

    @PostMapping({"","/"})
    public ResponseEntity<HttpStatus> add(@RequestBody AddCommentLikeRequest request) {
        commentLikeService.insert(request);
        commentService.updateLike(request.getCommentId(), 1);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id, @RequestBody AddCommentLikeRequest request) {
        commentService.updateLike(request.getCommentId(), -1);
        commentLikeService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
