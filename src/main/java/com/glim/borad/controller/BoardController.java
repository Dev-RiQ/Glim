package com.glim.borad.controller;

import com.glim.borad.dto.request.AddBoardRequest;
import com.glim.borad.dto.response.ViewBoardResponse;
import com.glim.borad.dto.response.ViewMyPageBoardResponse;
import com.glim.borad.service.BoardSaveService;
import com.glim.borad.service.BoardService;
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


    @GetMapping({"","/{offset}"})
    public StatusResponseDTO getMainBoard(@AuthenticationPrincipal SecurityUserDto user, @PathVariable(required = false) Long offset){
        List<ViewBoardResponse> list = boardService.getMainBoard(user.getId(), offset);
        ViewBoardResponse advertisement = boardService.getRandomAdvertisement(user.getId());
        if(advertisement != null){
            list.add(advertisement);
        }
        return StatusResponseDTO.ok(list);
    }

    @GetMapping({"/my/{id}", "/my/{id}/{offset}"})
    public StatusResponseDTO list(@PathVariable Long id, @PathVariable(required = false) Long offset) {
        List<ViewMyPageBoardResponse> board = boardService.myPageBoardList(id, offset);
        return StatusResponseDTO.ok(board);
    }

    @GetMapping("/show/{id}")
    public StatusResponseDTO show(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDto user) {
        ViewBoardResponse board = boardService.getBoard(user.getId(), id);
        return StatusResponseDTO.ok(board);
    }

    @GetMapping("/shorts/show/{id}")
    public StatusResponseDTO shorts(@PathVariable Long id, @AuthenticationPrincipal SecurityUserDto user) {
        ViewBoardResponse shorts = boardService.getShorts(user.getId(), id);
        return StatusResponseDTO.ok(shorts);
    }

    @GetMapping({"/shorts","/shorts/{offset}"})
    public StatusResponseDTO listShorts(@PathVariable(required = false) Long offset, @AuthenticationPrincipal SecurityUserDto user) {
        List<ViewBoardResponse> list = boardService.getShortsList(offset, user.getId());
        return StatusResponseDTO.ok(list);
    }

    @GetMapping({"/myShorts/{id}","/myShorts/{id}/{offset}"})
    public StatusResponseDTO MyShortsList(@PathVariable Long id, @PathVariable(required = false) Long offset) {
        List<ViewMyPageBoardResponse> list = boardService.myPageShortsList(id,offset);
        return StatusResponseDTO.ok(list);
    }

    @GetMapping({"/tag/{id}","/tag/{id}/{offset}"})
    public StatusResponseDTO getTagList(@PathVariable Long id, @PathVariable(required = false) Long offset){
        List<ViewMyPageBoardResponse> tagList = boardService.myPageTagsList(id,offset);
        return StatusResponseDTO.ok(tagList);
    }

    @GetMapping({"/search","/search/{offset}"})
    public StatusResponseDTO search(@PathVariable(required = false) Long offset) {
        List<ViewMyPageBoardResponse> list = boardService.allList(offset);
        return StatusResponseDTO.ok(list);
    }

    @GetMapping({"/save","/save/{offset}"})
    public StatusResponseDTO getSaveList(@PathVariable(required = false) Long offset, @AuthenticationPrincipal SecurityUserDto user) {
        List<ViewMyPageBoardResponse> saveList = boardService.myPageSaveList(user.getId(),offset);
        return StatusResponseDTO.ok(saveList);
    }

    @PostMapping("")
    public StatusResponseDTO add(@RequestBody AddBoardRequest request, @AuthenticationPrincipal SecurityUserDto user) {
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
