package com.glim.borad.controller;

import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardRequest;
import com.glim.borad.dto.request.UpdateBoardRequest;
import com.glim.borad.dto.response.ViewBoardResponse;
import com.glim.borad.service.BoardService;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;


    @GetMapping({"/main","/main/{offset}"})
    public StatusResponseDTO getMainBoard(@AuthenticationPrincipal SecurityUserDto user, @PathVariable(required = false) Long offset){
        List<ViewBoardResponse> list = boardService.getMainBoard(user.getId(), offset);
        return StatusResponseDTO.ok(list);
    }

    @GetMapping({"/{userId}", "/{userId}/{offset}"})
    public StatusResponseDTO list(@PathVariable Long userId, @PathVariable(required = false) Long offset) {
        List<ViewBoardResponse> board = boardService.list(userId, offset);
        Boards advertisement = boardService.getRandomAdvertisement();
        board.add(new ViewBoardResponse(advertisement));
        return StatusResponseDTO.ok(board);
    }

//    @GetMapping("/tag")
//    public StatusResponseDTO getTagList(@AuthenticationPrincipal SecurityUserDto user){
//        List<ViewBoardResponse> tagList = boardService.getTagList(user.getId());
//        return StatusResponseDTO.ok(tagList);
//    }

    @PostMapping({"", "/{userId}"})
    public StatusResponseDTO add(@RequestBody AddBoardRequest request, @PathVariable Long userId) {
        for(int i = 0; i < request.getImg().size(); i++) {

        }
        request.setUserId(userId);
        boardService.insert(request);
        return StatusResponseDTO.ok("게시물 추가 완료");
    }

    @PutMapping("/{id}")
    public StatusResponseDTO update(@PathVariable Long id, @RequestBody UpdateBoardRequest request) {
        boardService.update(id, request);
        return StatusResponseDTO.ok("게시물 수정 완료");
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDto user) {
        boolean isUser = boardService.isLoginUser(id, user.getId());
        if(!isUser || !user.getNickname().equals("admin")) {
            return null;
        }
        boardService.delete(id);
        return StatusResponseDTO.ok("게시물 삭제 완료");
    }
}
