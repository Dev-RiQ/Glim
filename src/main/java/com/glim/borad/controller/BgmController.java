package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBgmRequest;
import com.glim.borad.dto.response.ViewBgmResponse;
import com.glim.borad.service.BgmService;
import com.glim.borad.service.BoardService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bgm")
public class BgmController {

    private final BgmService bgmService;
    private final BoardService boardService;

    @GetMapping({"", "/{id}"})
    public StatusResponseDTO list(@PathVariable Long id, @PathVariable(required = false) Long offset) {
        List<ViewBgmResponse> board = bgmService.list(id, offset);
        return StatusResponseDTO.ok(board);
    }

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddBgmRequest request) {
        bgmService.insert(request);
        return StatusResponseDTO.ok("Bgm 추가 완료");
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDto user) {
        boolean isUser = boardService.isLoginUser(id, user.getId());
        if(!isUser || !user.getNickname().equals("admin")) {
            return null;
        }
        bgmService.delete(id);
        return StatusResponseDTO.ok("Bgm 삭제 완료");
    }
}
