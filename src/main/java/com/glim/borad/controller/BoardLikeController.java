package com.glim.borad.controller;

import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardLikeRequest;
import com.glim.borad.service.BoardService;
import com.glim.borad.service.BoardLikeService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardLike")
public class BoardLikeController {

    private final BoardLikeService boardLikeService;
    private final BoardService boardService;

    @PostMapping({"/{boardId}"})
    public StatusResponseDTO add(@PathVariable Long boardId, @AuthenticationPrincipal SecurityUserDto user) {
        if(boardLikeService.insert(boardId, user.getId())){
            boardService.updateLike(boardId, 1);
        }
        return StatusResponseDTO.ok("좋아요 완료");
    }

    @DeleteMapping("/{boardId}")
    public StatusResponseDTO delete(@PathVariable Long boardId, @AuthenticationPrincipal SecurityUserDto user) {
        if(boardLikeService.delete(boardId, user.getId())){
            boardService.updateLike(boardId, -1);
        }
        return StatusResponseDTO.ok("좋아요 취소 완료");
    }

}
