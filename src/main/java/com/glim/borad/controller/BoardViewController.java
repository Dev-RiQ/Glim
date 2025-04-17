package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBoardViewRequest;
import com.glim.borad.service.BoardService;
import com.glim.borad.service.BoardViewService;
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
    public ResponseEntity<HttpStatus> add(@RequestBody AddBoardViewRequest request) {
        boardViewService.insert(request);
        boardService.updateView(request.getBoardId(), 1);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
