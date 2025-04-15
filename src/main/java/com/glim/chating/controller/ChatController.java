package com.glim.chating.controller;

import com.glim.chating.dto.request.AddChatRoomRequest;
import com.glim.chating.dto.response.PreviewChatMsgResponse;
import com.glim.chating.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

//    @GetMapping({"","/{id}"})
//    public ResponseEntity<PreviewChatMsgResponse> getDummy(@PathVariable(required = false) Long id) {
//        PreviewChatMsgResponse chat = chatService.findDummyById(id);
//        return ResponseEntity.ok(chat);
//    }
//
//    @PostMapping({"","/"})
//    public ResponseEntity<HttpStatus> join(AddChatRoomRequest request) {
//        chatService.insert(request);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<HttpStatus> update(@PathVariable Long id, UpdateChatMsgRequest request) {
//        chatService.update(id, request);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
//        chatService.delete(id);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
}
