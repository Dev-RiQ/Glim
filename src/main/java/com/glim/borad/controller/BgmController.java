package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBgmRequest;
import com.glim.borad.dto.response.ViewBgmResponse;
import com.glim.borad.dto.response.ViewBoardResponse;
import com.glim.borad.service.BgmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bgm")
public class BgmController {

    private final BgmService bgmService;

    @GetMapping({"","/{id}"})
    public List<ViewBgmResponse> list() {
        List<ViewBgmResponse> board = bgmService.list();
        return ResponseEntity.ok(board).getBody();
    }

    @PostMapping({"","/"})
    public ResponseEntity<HttpStatus> add(@RequestBody AddBgmRequest request) {
        bgmService.insert(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        bgmService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
