package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBoardTagRequest;
import com.glim.borad.dto.request.UpdateBoardTagRequest;
import com.glim.borad.service.BoardTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardTag")
public class BoardTagController {

    private final BoardTagService boardTagService;

    @PostMapping({"","/"})
    public ResponseEntity<HttpStatus> add(@RequestBody AddBoardTagRequest request) {
        boardTagService.insert(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody UpdateBoardTagRequest request) {
        boardTagService.update(id, request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        boardTagService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
