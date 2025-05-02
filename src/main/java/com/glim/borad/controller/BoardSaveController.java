package com.glim.borad.controller;

import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardSaveRequest;
import com.glim.borad.repository.BoardSaveRepository;
import com.glim.borad.service.BoardSaveService;
import com.glim.borad.service.BoardService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardSave")
public class BoardSaveController {

    private final BoardSaveService boardSaveService;
    private final BoardService boardService;

    // 저장한 게시물 목록
    @GetMapping("")
    public StatusResponseDTO getSaveList(@AuthenticationPrincipal SecurityUserDto user) {
        List<Long> boardIdList = boardSaveService.getSaveList(user.getId());
        List<Boards> boardList = boardService.getSaveList(boardIdList);
        return StatusResponseDTO.ok(boardList);
    }

    @PostMapping("/{boardId}")
    public StatusResponseDTO add(@PathVariable Long boardId, @AuthenticationPrincipal SecurityUserDto user) {
        boardSaveService.insert(boardId, user.getId());
        return StatusResponseDTO.ok("게시물 저장 완료");
    }

    @DeleteMapping("/{boardId}")
    public StatusResponseDTO delete(@PathVariable Long boardId, @AuthenticationPrincipal SecurityUserDto user) {
        boardSaveService.delete(boardId, user.getId());
        return StatusResponseDTO.ok("게시물 저장 취소 완료");
    }
}
