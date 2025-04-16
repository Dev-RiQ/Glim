package com.glim.chating.controller;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.dto.request.AddChatMsgRequest;
import com.glim.chating.dto.request.AddChatRoomRequest;
import com.glim.chating.dto.response.ViewChatMsgResponse;
import com.glim.chating.dto.response.ViewChatRoomResponse;
import com.glim.chating.dto.response.ViewChatUserResponse;
import com.glim.chating.service.ChatService;
import com.glim.common.kafka.dto.Message;
import com.glim.common.statusResponse.StatusResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    private List<ChatMsg> dummy(){
        List<ChatMsg> testList = new ArrayList<>();
        testList.add(new ChatMsg(1L, 1L, "test", 0L));
        testList.add(new ChatMsg(2L, 2L, "test", 0L));
        testList.add(new ChatMsg(3L, 3L, "test", 0L));
        testList.add(new ChatMsg(4L, 4L, "test", 0L));
        return testList;
    }

    @GetMapping("")
    public StatusResponseDTO getUserChatRoomList() {
        List<ViewChatRoomResponse> userChatRoomList = chatService.findChatRoomListByUserId();
        return StatusResponseDTO.ok(dummy());
    }

    @GetMapping({"/{roomId}","/{roomId}-{offset}"})
    public StatusResponseDTO getRoomChatMsgList(@PathVariable Long roomId, @PathVariable(required = false) Long offset) {
        List<ViewChatMsgResponse> roomChatMsgList = chatService.findChatMsgListByRoomId(roomId, offset);
        return StatusResponseDTO.ok(roomChatMsgList);
    }

    @GetMapping("/users/{roomId}")
    public StatusResponseDTO getChatRoomUserList(@PathVariable Long roomId) {
        List<ViewChatUserResponse> chatRoomUserList = chatService.findUserListByRoomId(roomId);
        return StatusResponseDTO.ok(chatRoomUserList);
    }

    @PostMapping("/room")
    public StatusResponseDTO createChatRoom(@RequestBody AddChatRoomRequest addChatRoomRequest) {
        ViewChatRoomResponse chatRoom = chatService.createChatRoom(addChatRoomRequest);
        return StatusResponseDTO.ok(chatRoom);
    }

    @PostMapping("/sendMsg")
    public StatusResponseDTO sendMessage(@RequestBody AddChatMsgRequest addChatMsgRequest) {
        chatService.sendMessage(addChatMsgRequest);
        return StatusResponseDTO.ok();
    }

    @DeleteMapping("/{roomId}")
    public StatusResponseDTO deleteChatRoom(@PathVariable String roomId) {
        chatService.deleteChatRoom(roomId);
        return StatusResponseDTO.ok();
    }

}
