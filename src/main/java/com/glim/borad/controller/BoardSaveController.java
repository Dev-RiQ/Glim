package com.glim.borad.controller;

import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardSaveRequest;
import com.glim.borad.repository.BoardSaveRepository;
import com.glim.borad.service.BoardSaveService;
import com.glim.borad.service.BoardService;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardSave")
public class BoardSaveController {

    private final BoardSaveService boardSaveService;
    private final BoardService boardService;

    @GetMapping("")
    public StatusResponseDTO getSaveList(@RequestParam Long userId) {
        List<Long> boardIdList = boardSaveService.getSaveList(userId);
        List<Boards> boardList = boardService.getSaveList(boardIdList);
        return StatusResponseDTO.ok(boardList);
    }

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddBoardSaveRequest request) {
        boardSaveService.insert(request);
        return StatusResponseDTO.ok("게시물 저장 완료");
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@RequestBody AddBoardSaveRequest request) {
        boardSaveService.delete(request.getBoardId(), request.getUserId());
        return StatusResponseDTO.ok("게시물 저장 취소 완료");
    }
}
