package com.glim.borad.controller;

import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardLikeRequest;
import com.glim.borad.service.BoardService;
import com.glim.borad.service.BoardLikeService;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardLike")
public class BoardLikeController {

    private final BoardLikeService boardLikeService;
    private final BoardService boardService;

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddBoardLikeRequest request) {
        boardLikeService.insert(request);
        Boards board = boardService.updateLike(request.getBoardId(), 1);
        return StatusResponseDTO.ok(board);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@RequestBody AddBoardLikeRequest request, @PathVariable Long id) {
        boardLikeService.delete(id);
        boardService.updateLike(request.getBoardId(), -1);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
