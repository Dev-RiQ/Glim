package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBoardTagRequest;
import com.glim.borad.dto.request.UpdateBoardTagRequest;
import com.glim.borad.service.BoardTagService;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardTag")
public class BoardTagController {

    private final BoardTagService boardTagService;

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddBoardTagRequest request) {
//        boardTagService.insert(request);
        return StatusResponseDTO.ok("테그 완료");
    }

    @PutMapping("/{id}")
    public StatusResponseDTO update(@PathVariable Long id, @RequestBody UpdateBoardTagRequest request) {
        boardTagService.update(id, request);
        return StatusResponseDTO.ok("테그 수정 완료");
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id) {
        boardTagService.delete(id);
        return StatusResponseDTO.ok("테그 삭제 완료");
    }

}
