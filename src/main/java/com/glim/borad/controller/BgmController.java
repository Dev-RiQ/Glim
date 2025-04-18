package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBgmRequest;
import com.glim.borad.dto.response.ViewBgmResponse;
import com.glim.borad.service.BgmService;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bgm")
public class BgmController {

    private final BgmService bgmService;

    @GetMapping({"","/{id}"})
    public StatusResponseDTO list() {
        List<ViewBgmResponse> board = bgmService.list();
        return StatusResponseDTO.ok(board);
    }

    @PostMapping({"","/"})
    public StatusResponseDTO add(@RequestBody AddBgmRequest request) {
        bgmService.insert(request);
        return StatusResponseDTO.ok();
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id) {
        bgmService.delete(id);
        return StatusResponseDTO.ok();
    }
}
