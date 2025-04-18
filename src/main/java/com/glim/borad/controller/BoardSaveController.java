package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBoardSaveRequest;
import com.glim.borad.service.BoardSaveService;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardSave")
public class BoardSaveController {

    private final BoardSaveService boardSaveService;

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddBoardSaveRequest request) {
        boardSaveService.insert(request);
        return StatusResponseDTO.ok();
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id) {
        boardSaveService.delete(id);
        return StatusResponseDTO.ok();
    }
}
