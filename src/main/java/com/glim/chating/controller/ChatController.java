package com.glim.chating.controller;

import com.glim.chating.dto.request.AddChatMsgRequest;
import com.glim.chating.dto.response.ViewChatMsgResponse;
import com.glim.chating.dto.response.ViewChatRoomResponse;
import com.glim.chating.dto.response.ViewChatUserResponse;
import com.glim.chating.service.ChatMsgService;
import com.glim.chating.service.ChatRoomService;
import com.glim.chating.service.ChatUserService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.common.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMsgService chatMsgService;
    private final ChatRoomService chatRoomService;
    private final ChatUserService chatUserService;

    @GetMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(HttpServletResponse response) {
        return chatRoomService.getEmitter(response);
    }

    @GetMapping({"/{roomId}","/{roomId}/{offset}"})
    public StatusResponseDTO getRoomChatMsgList(@PathVariable Long roomId, @PathVariable(required = false) Long offset, @AuthenticationPrincipal SecurityUserDto user) {
        List<ViewChatMsgResponse> roomChatMsgList = chatMsgService.findChatMsgListByRoomId(roomId, offset, user);
        Map<String, Object> map = new HashMap<>();
        map.put("msgList", roomChatMsgList);
        map.put("loginId", user.getId());
        return StatusResponseDTO.ok(map);
    }

    @GetMapping("/users/{roomId}")
    public StatusResponseDTO getChatRoomUser(@PathVariable Long roomId, @AuthenticationPrincipal SecurityUserDto user) {
        ViewChatUserResponse chatRoomUser = chatUserService.findByRoomId(roomId, user);
        return StatusResponseDTO.ok(chatRoomUser);
    }

    @PostMapping("/room/{joinUserId}")
    public StatusResponseDTO createChatRoom(@PathVariable Long joinUserId, @AuthenticationPrincipal SecurityUserDto user) {
        ViewChatRoomResponse chatRoom = chatRoomService.createChatRoom(joinUserId, user);
        return StatusResponseDTO.ok(chatRoom);
    }

    @PostMapping("/sendMsg")
    public StatusResponseDTO sendMessage(@RequestBody AddChatMsgRequest addChatMsgRequest, @AuthenticationPrincipal SecurityUserDto user) {
        chatMsgService.sendMessage(addChatMsgRequest, user);
        return StatusResponseDTO.ok("메시지 전송 성공");
    }

    @PutMapping("/user/{roomId}")
    public StatusResponseDTO readMsg(@PathVariable Long roomId, @AuthenticationPrincipal SecurityUserDto user) {
        chatUserService.updateChatUserReadMsg(roomId, user);
        return StatusResponseDTO.ok("메시지 읽음 처리");
    }

    @PutMapping("/exit/{roomId}")
    public StatusResponseDTO escapeChatRoom(@PathVariable Long roomId, @AuthenticationPrincipal SecurityUserDto user) {
        chatUserService.escapeChatRoom(roomId, user);
        return StatusResponseDTO.ok("채팅방 나가기 성공");
    }

}
