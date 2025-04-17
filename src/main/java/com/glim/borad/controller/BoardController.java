package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBoardRequest;
import com.glim.borad.dto.request.UpdateBoardRequest;
import com.glim.borad.dto.response.ViewBoardResponse;
import com.glim.borad.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
/*@Controller*/
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

     @GetMapping({"","/{id}"})
    public List<ViewBoardResponse> list(@PathVariable(required = false) Long id) {
        List<ViewBoardResponse> dummy = boardService.list(id);
        return ResponseEntity.ok(dummy).getBody();
    }

    @PostMapping({"","/"})
    public ResponseEntity<HttpStatus> add(AddBoardRequest request) {
        boardService.insert(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

     @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, UpdateBoardRequest request) {
        boardService.update(id, request);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
