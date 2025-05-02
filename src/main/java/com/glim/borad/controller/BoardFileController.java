package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBoardFileRequest;
import com.glim.borad.service.BoardFileSevice;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardFile")
public class BoardFileController {

    private final BoardFileSevice boardFileSevice;

//    @PostMapping({"","/"})
//    public StatusResponseDTO add(@RequestBody AddBoardFileRequest request) {
////        boardFileSevice.insert(request);
//        return StatusResponseDTO.ok("파일 저장 완료");
//    }
//
//    @DeleteMapping("/{id}")
//    public StatusResponseDTO delete(@PathVariable Long id,@PathVariable Long boardId) {
//        boardFileSevice.delete(id, boardId);
//        return StatusResponseDTO.ok("파일 삭제 완료");
//    }
}
