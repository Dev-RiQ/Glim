package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBoardFileRequest;
import com.glim.borad.dto.request.UpdateBoardFileRequest;
import com.glim.borad.service.BoardFileSevice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardFile")
public class BoardFileController {

    private final BoardFileSevice boardFileSevice;

    @PostMapping({"","/"})
    public ResponseEntity<HttpStatus> add(@RequestBody AddBoardFileRequest request) {
        boardFileSevice.insert(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody UpdateBoardFileRequest request) {
        boardFileSevice.update(id, request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        boardFileSevice.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
