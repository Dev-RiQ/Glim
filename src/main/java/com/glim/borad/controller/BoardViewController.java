package com.glim.borad.controller;

import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardViewRequest;
import com.glim.borad.service.BoardService;
import com.glim.borad.service.BoardViewService;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardView")
public class BoardViewController {

    private final BoardViewService boardViewService;
    private final BoardService boardService;

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddBoardViewRequest request) {
        boardViewService.insert(request);
        Boards board = boardService.updateView(request.getBoardId(), 1);
        return StatusResponseDTO.ok("게시물 조회수 추가 완료");
    }
}
