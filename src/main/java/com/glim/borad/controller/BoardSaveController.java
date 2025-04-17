package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBoardSaveRequest;
import com.glim.borad.service.BoardSaveService;
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
    public ResponseEntity<HttpStatus> add(@RequestBody AddBoardSaveRequest request) {
        boardSaveService.insert(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        boardSaveService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
