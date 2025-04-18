package com.glim.borad.controller;

import com.glim.borad.domain.BoardTags;
import com.glim.borad.dto.request.AddBoardTagRequest;
import com.glim.borad.dto.request.UpdateBoardTagRequest;
import com.glim.borad.service.BoardTagService;
import com.glim.common.statusResponse.StatusResponseDTO;
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
    public StatusResponseDTO add(@RequestBody AddBoardTagRequest request) {
        boardTagService.insert(request);
        return StatusResponseDTO.ok();
    }

    @PutMapping("/{id}")
    public StatusResponseDTO update(@PathVariable Long id, @RequestBody UpdateBoardTagRequest request) {
        BoardTags tag = boardTagService.update(id, request);
        return StatusResponseDTO.ok(tag);
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id) {
        boardTagService.delete(id);
        return StatusResponseDTO.ok();
    }

}
