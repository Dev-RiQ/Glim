package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBoardRequest;
import com.glim.borad.dto.request.UpdateBoardRequest;
import com.glim.borad.dto.response.ViewBoardResponse;
import com.glim.borad.service.BoardService;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AwsS3Util awsS3Util;

    @GetMapping({"/{userId}", "/{userId}/{offset}"})
    public StatusResponseDTO list(@PathVariable Long userId, @PathVariable(required = false) Long offset) {
        List<ViewBoardResponse> board = boardService.list(userId, offset);
        System.out.println(board.get(9).getId() == null ? "id = " + "null" : "id = " + board.get(9).getId());
        for (int i = 0; i < 10; i++) {
            offset = board.get(i).getId() == null ? -1 : board.get(i).getId();
            if(offset == -1){
                break;
            }
        }
        return StatusResponseDTO.ok(board);
    }

    @PostMapping({"", "/"})
    public StatusResponseDTO add(@RequestBody AddBoardRequest request) {
        boardService.insert(request);
        return StatusResponseDTO.ok();
    }

    @PutMapping("/{id}")
    public StatusResponseDTO update(@PathVariable Long id, @RequestBody UpdateBoardRequest request) {
        boardService.update(id, request);
        return StatusResponseDTO.ok();
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id) {
        boardService.delete(id);
        return StatusResponseDTO.ok();
    }
}
