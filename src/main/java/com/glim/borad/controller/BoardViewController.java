package com.glim.borad.controller;

import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardViewRequest;
import com.glim.borad.service.BoardService;
import com.glim.borad.service.BoardViewService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardView")
public class BoardViewController {

    private final BoardViewService boardViewService;
    private final BoardService boardService;

    @PostMapping({"/{boardId}"})
    public StatusResponseDTO add(@PathVariable Long boardId, @AuthenticationPrincipal SecurityUserDto user) {
        if(boardViewService.insert(new AddBoardViewRequest(boardId, user.getId()))){
            boardService.updateView(boardId, 1);
        }
        return StatusResponseDTO.ok("게시물 조회수 추가 완료");
    }
}
