package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBoardShareRequest;
import com.glim.borad.service.BoardService;
import com.glim.borad.service.BoardShareService;
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
    public ResponseEntity<HttpStatus> add(@RequestBody AddBoardShareRequest request) {
        boardShareService.insert(request);
        boardService.updateShare(request.getBoardId(), 1);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@RequestBody AddBoardShareRequest request, @PathVariable Long id) {
        boardShareService.delete(id);
        boardService.updateShare(request.getBoardId(), -1);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
