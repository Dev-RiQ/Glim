package com.glim.admin.controller;

import com.glim.borad.dto.request.AddBgmRequest;
import com.glim.borad.service.BgmService;
import com.glim.borad.service.BoardService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminBgmController {

    private final BgmService bgmService;
    private final BoardService boardService;

    @PostMapping({"","/"})
    public StatusResponseDTO add(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDto user, @RequestBody AddBgmRequest request) {
        boolean isUser = boardService.isLoginUser(id, user.getId());
        if(!isUser || !user.getNickname().equals("admin")) {
            return null;
        }
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
