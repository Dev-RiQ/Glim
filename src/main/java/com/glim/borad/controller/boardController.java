package com.glim.borad.controller;

import com.glim.borad.dto.request.addBoardRequest;
import com.glim.borad.dto.request.updateBoardRequest;
import com.glim.borad.dto.response.viewBoardResponse;
import com.glim.borad.service.boardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class boardController {

    private final boardService boardService;

     @GetMapping({"","/{id}"})
    public List<viewBoardResponse> list(@PathVariable(required = false) Long id) {
        List<viewBoardResponse> dummy = boardService.list(id);
        return ResponseEntity.ok(dummy).getBody();
    }

    @PostMapping({"","/"})
    public ResponseEntity<HttpStatus> add(addBoardRequest request) {
        boardService.insert(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

     @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, updateBoardRequest request) {
        boardService.update(id, request);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
