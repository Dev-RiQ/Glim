package com.glim.borad.controller;

import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardRequest;
import com.glim.borad.dto.request.UpdateBoardRequest;
import com.glim.borad.dto.response.ViewBoardResponse;
import com.glim.borad.dto.response.ViewMyPageBoardResponse;
import com.glim.borad.service.BoardSaveService;
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
    private final BoardSaveService boardSaveService;


    @GetMapping({"/main","/main/{offset}"})
    public StatusResponseDTO getMainBoard(@AuthenticationPrincipal SecurityUserDto user, @PathVariable(required = false) Long offset){
        List<ViewBoardResponse> list = boardService.getMainBoard(user.getId(), offset);
        Boards advertisement = boardService.getRandomAdvertisement();
        list.add(new ViewBoardResponse(advertisement));
        return StatusResponseDTO.ok(list);
    }

    @GetMapping({"/my/{id}", "/my/{id}/{offset}"})
    public StatusResponseDTO list(@PathVariable(required = false) Long offset, @AuthenticationPrincipal SecurityUserDto user) {
        List<ViewMyPageBoardResponse> board = boardService.list(user.getId(), offset);
        Boards advertisement = boardService.getRandomAdvertisement();
        board.add(new ViewMyPageBoardResponse(advertisement));
        return StatusResponseDTO.ok(board);
    }

    @GetMapping("/{id}")
    public StatusResponseDTO show(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDto user) {
        ViewBoardResponse board = boardService.getBoard(id);
        return StatusResponseDTO.ok(board);
    }

    @GetMapping("/shorts/{id}")
    public StatusResponseDTO shorts(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDto user) {
        ViewBoardResponse shorts = boardService.getShorts(id);
        return StatusResponseDTO.ok(shorts);
    }

    @GetMapping({"/shorts","/shorts/{offset}"})
    public StatusResponseDTO listShorts(@PathVariable(required = false) Long offset, @AuthenticationPrincipal SecurityUserDto user) {
        List<ViewBoardResponse> list = boardService.getShortsList(offset, user.getId());
        return StatusResponseDTO.ok();
    }

    @GetMapping({"/myShorts/{id}","/myShorts/{id}/{offset}"})
    public StatusResponseDTO MyShortsList(@PathVariable(required = false) Long offset, @AuthenticationPrincipal SecurityUserDto user) {
        List<ViewBoardResponse> list = boardService.getMyShortsList(offset, user.getId());
        return StatusResponseDTO.ok();
    }

    @GetMapping({"/board/search","/board/search/{offset}"})
    public StatusResponseDTO search(@PathVariable Long offset, @AuthenticationPrincipal SecurityUserDto user) {
        List<ViewBoardResponse> list = boardService.allList(offset);
        return StatusResponseDTO.ok(list);
    }

//    @GetMapping("/tag")
//    public StatusResponseDTO getTagList(@AuthenticationPrincipal SecurityUserDto user){
//        List<ViewBoardResponse> tagList = boardService.getTagList(user.getId());
//        return StatusResponseDTO.ok(tagList);
//    }

    @PostMapping("")
    public StatusResponseDTO add(@RequestBody AddBoardRequest request, @AuthenticationPrincipal SecurityUserDto user) {
        for(int i = 0; i < request.getImg().size(); i++) {

        }
        request.setUserId(user.getId());
        boardService.insert(request);
        return StatusResponseDTO.ok("게시물 추가 완료");
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDto user) {
        boolean isUser = boardService.isLoginUser(id, user.getId());
        if(!isUser || !user.getNickname().equals("admin")) {
            return null;
        }
        boardSaveService.delete(id, user.getId());
        boardService.delete(id);
        return StatusResponseDTO.ok("게시물 삭제 완료");
    }
}
