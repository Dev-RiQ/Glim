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
@Controller
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping({"","/{id}"})
    public  List<ViewBoardResponse> list() {
        List<ViewBoardResponse> board = boardService.list(); // 추후에 유저id로 검색으로 수정
        return ResponseEntity.ok(board).getBody();
    }

    @PostMapping({"","/"})
    public ResponseEntity<HttpStatus> add(@RequestBody AddBoardRequest request) {
        boardService.insert(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

     @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody UpdateBoardRequest request) {
        boardService.update(id, request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

     @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
