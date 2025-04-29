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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("")
    public StatusResponseDTO getUserChatRoomList() {
        List<ViewChatRoomResponse> userChatRoomList = chatRoomService.findChatRoomListByUserId();
        return StatusResponseDTO.ok(userChatRoomList);
    }

    @GetMapping({"/{roomId}","/{roomId}/{offset}"})
    public StatusResponseDTO getRoomChatMsgList(@PathVariable Long roomId, @PathVariable(required = false) Long offset) {
        List<ViewChatMsgResponse> roomChatMsgList = chatMsgService.findChatMsgListByRoomId(roomId, offset);
        Map<String, Object> map = new HashMap<>();
        map.put("msgList", roomChatMsgList);
        map.put("loginId", SecurityUtil.getUser().getId());
        return StatusResponseDTO.ok(map);
    }

    @GetMapping("/users/{roomId}")
    public StatusResponseDTO getChatRoomUser(@PathVariable Long roomId) {
        ViewChatUserResponse chatRoomUser = chatUserService.findByRoomId(roomId);
        return StatusResponseDTO.ok(chatRoomUser);
    }

    @PostMapping("/room/{joinUserId}")
    public StatusResponseDTO createChatRoom(@PathVariable Long joinUserId) {
        ViewChatRoomResponse chatRoom = chatRoomService.createChatRoom(joinUserId);
        return StatusResponseDTO.ok(chatRoom);
    }

    @PostMapping("/sendMsg")
    public StatusResponseDTO sendMessage(@RequestBody AddChatMsgRequest addChatMsgRequest) {
        chatMsgService.sendMessage(addChatMsgRequest);
        return StatusResponseDTO.ok();
    }

    @PutMapping("/user/{roomId}")
    public StatusResponseDTO readMsg(@PathVariable Long roomId) {
        chatUserService.updateChatUserReadMsg(roomId);
        return StatusResponseDTO.ok();
    }

    @PutMapping("/exit/{roomId}")
    public StatusResponseDTO escapeChatRoom(@PathVariable Long roomId) {
        chatUserService.escapeChatRoom(roomId);
        return StatusResponseDTO.ok();
    }

}
