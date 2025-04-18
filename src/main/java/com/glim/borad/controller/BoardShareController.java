package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBoardShareRequest;
import com.glim.borad.service.BoardService;
import com.glim.borad.service.BoardShareService;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardShare")
public class BoardShareController {

    private final BoardShareService boardShareService;
    private final BoardService boardService;

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddBoardShareRequest request) {
        boardShareService.insert(request);
        boardService.updateShare(request.getBoardId(), 1);
        return StatusResponseDTO.ok();
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@RequestBody AddBoardShareRequest request, @PathVariable Long id) {
        boardShareService.delete(id);
        boardService.updateShare(request.getBoardId(), -1);
        return StatusResponseDTO.ok();
    }
}
