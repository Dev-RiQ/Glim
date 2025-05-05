package com.glim.chating.service;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
import com.glim.chating.domain.ChatUser;
import com.glim.chating.dto.request.AddChatMsgRequest;
import com.glim.chating.dto.request.AddChatRoomRequest;
import com.glim.chating.dto.request.AddChatUserRequest;
import com.glim.chating.dto.response.ViewChatMsgResponse;
import com.glim.chating.dto.response.ViewChatRoomResponse;
import com.glim.chating.dto.response.ViewChatUserResponse;
import com.glim.chating.repository.ChatMsgRepository;
import com.glim.chating.repository.ChatRoomRepository;
import com.glim.chating.repository.ChatUserRepository;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.kafka.dto.Message;
import com.glim.common.kafka.service.SendMessage;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.utils.SecurityUtil;
import com.glim.notification.domain.Notification;
import com.glim.notification.dto.response.NotificationResponse;
import com.glim.stories.service.StoryService;
import com.glim.user.domain.User;
import com.glim.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatMsgRepository chatMsgRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final StoryService storyService;
    private final AwsS3Util awsS3Util;
    private static final Long DEFAULT_TIMEOUT = 3600000L;
    private SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    private final ExecutorService taskExecutor = Executors.newSingleThreadExecutor();

    public SseEmitter getEmitter(final HttpServletResponse response) {
        List<ViewChatRoomResponse> chatRoomList = findChatRoomListByUserId();
        if(!chatRoomList.isEmpty()) {
            emitter = new SseEmitter(DEFAULT_TIMEOUT);
        }
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        sendNotification(chatRoomList);
        return emitter;
    }

    private void sendNotification(Object data) {
        taskExecutor.execute(() -> {
            SseEmitter.SseEventBuilder event = SseEmitter.event();
            event.name("chat")
                    .data(data);
            try{
                emitter.send(event);
                emitter.complete();
                log.info("chat list sent successfully");
            }catch (Exception e){
                emitter.completeWithError(e);
                log.error("chat list sent failed");
            }
        });
    }

    public List<ViewChatRoomResponse> findChatRoomListByUserId() {
        SecurityUserDto user = SecurityUtil.getUser();
        List<ChatUser> chatUserList = chatUserRepository.findAllByUserId(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NO_CREATED));
        return getViewChatRoomListByUserId(chatUserList);
    }

    private List<ViewChatRoomResponse> getViewChatRoomListByUserId(List<ChatUser> chatUserList) {
        List<ViewChatRoomResponse> chatRoomList = new ArrayList<>();
        User user = null;
        for(ChatUser chatUser : chatUserList) {
            if(chatUser.getValid().toString().equals("OUT")) continue;
            ChatRoom chatRoom = chatRoomRepository.findById(chatUser.getRoomId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
            ChatUser chatUserNotMe = chatUserRepository.findByRoomIdAndUserIdNot(chatRoom.getId(), chatUser.getUserId())
                    .orElse(null);
            if(chatUserNotMe != null) {
                user = userRepository.findById(chatUserNotMe.getUserId())
                    .orElse(null);
            }
            List<ChatMsg> chatMsgList = chatMsgRepository.findAllByRoomIdOrderByMsgIdDesc(chatUser.getRoomId(), Limit.of(1));
            ChatMsg chatMsg = chatMsgList.isEmpty() ? null :chatMsgList.get(0);
            Long readId = chatUserRepository.findByRoomIdAndUserId(chatUser.getRoomId(), chatUser.getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CHATUSER_NOT_FOUND)).getReadMsgId();
            ChatMsg lastId = chatMsgRepository.findTop1ByRoomIdOrderByMsgIdDesc(chatUser.getRoomId())
                    .orElse(null);
            boolean hasRead = lastId == null || readId.equals(lastId.getMsgId());
            boolean isStory = storyService.isStory(chatUser.getId());
            ViewChatUserResponse userView = user == null ? null : new ViewChatUserResponse(user, chatUser, isStory);
            if(userView != null ) userView.setImg(awsS3Util.getURL(userView.getImg(), FileSize.IMAGE_128));
            chatRoomList.add(new ViewChatRoomResponse(chatRoom, chatMsg, userView, hasRead));
        }
        return chatRoomList.stream().sorted(Comparator.comparing(ViewChatRoomResponse::getUpdatedAt)).toList();
    }

    public Boolean hasNewChat(){
        SecurityUserDto user = SecurityUtil.getUser();
        List<ChatUser> chatUserList = chatUserRepository.findAllByUserId(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHATUSER_NOT_FOUND));
        for(ChatUser chatUser : chatUserList) {
            if(chatUser.getValid().toString().equals("OUT")) continue;
            Long readId = chatUserRepository.findByRoomIdAndUserId(chatUser.getRoomId(), chatUser.getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CHATUSER_NOT_FOUND)).getReadMsgId();
            ChatMsg lastId = chatMsgRepository.findTop1ByRoomIdOrderByMsgIdDesc(chatUser.getRoomId())
                    .orElse(null);
            boolean hasRead = lastId == null || readId.equals(lastId.getMsgId());
            if(!hasRead) return true;
        }
        return false;
    }


    @Transactional
    public ViewChatRoomResponse createChatRoom(Long joinUserId) {
        ChatRoom createdRoom = existsChatRoom(joinUserId);
        if(createdRoom == null) {
            ChatRoom chatRoom = new AddChatRoomRequest().toEntity();
            createdRoom = chatRoomRepository.save(chatRoom);
            saveChatUser(createdRoom.getId(), joinUserId);
            chatRoomRepository.save(createdRoom);
        }
        SecurityUserDto me = SecurityUtil.getUser();
        User user = userRepository.findById(joinUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        ChatUser chatUser = chatUserRepository.findByRoomIdAndUserIdNot(createdRoom.getId(), me.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHATUSER_NOT_FOUND));
        boolean isStory = storyService.isStory(chatUser.getId());
        ViewChatUserResponse userView = new ViewChatUserResponse(user, chatUser, isStory);
        userView.setImg(awsS3Util.getURL(userView.getImg(), FileSize.IMAGE_128));

        return new ViewChatRoomResponse(createdRoom, userView);
    }

    private ChatRoom existsChatRoom(Long joinUserId) {
        SecurityUserDto user = SecurityUtil.getUser();
        List<ChatUser> userChatUserList = chatUserRepository.findAllByUserId(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHATUSER_NOT_FOUND));
        for(ChatUser chatUser : userChatUserList) {
            Long roomId = chatUser.getRoomId();
            ChatUser joinUser = chatUserRepository.findByRoomIdAndUserIdNot(roomId,user.getId()).orElse(null);
            if(joinUser != null && joinUser.getUserId() == joinUserId) {
                return chatRoomRepository.findById(roomId)
                        .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
            }
        }
        return null;
    }

    private void saveChatUser(Long roomId, Long joinUserId) {
        SecurityUserDto user = SecurityUtil.getUser();
        AddChatUserRequest addChatUserCreate = new AddChatUserRequest(roomId, user.getId());
        AddChatUserRequest addChatUserJoin = new AddChatUserRequest(roomId, joinUserId);
        chatUserRepository.save(new AddChatUserRequest().toEntity(addChatUserCreate));
        chatUserRepository.save(new AddChatUserRequest().toEntity(addChatUserJoin));
    }
}
