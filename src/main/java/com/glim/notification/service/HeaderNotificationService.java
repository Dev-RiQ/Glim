package com.glim.notification.service;

import com.glim.borad.repository.BoardFileRepository;
import com.glim.chating.service.ChatMsgService;
import com.glim.chating.service.ChatRoomService;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.utils.SecurityUtil;
import com.glim.notification.domain.Notification;
import com.glim.notification.domain.Type;
import com.glim.notification.dto.response.HeaderNotificationResponse;
import com.glim.notification.dto.response.NotificationResponse;
import com.glim.notification.repository.NotificationRepository;
import com.glim.stories.repository.StoryRepository;
import com.glim.user.domain.User;
import com.glim.user.repository.UserRepository;
import com.glim.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeaderNotificationService {

    private static final Long DEFAULT_TIMEOUT = 3600000L;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ExecutorService taskExecutor = Executors.newSingleThreadExecutor();
    private final ChatRoomService chatRoomService;
    private SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);


    public SseEmitter getEmitter(final HttpServletResponse response, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(ErrorCode::throwDummyNotFound);
        Boolean hasAlarm = notificationRepository.existsByUserIdAndIdGreaterThan(userId, user.getReadAlarmId());
        Boolean hasChat = chatRoomService.hasNewChat();
        emitter = new SseEmitter(DEFAULT_TIMEOUT);
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        sendNotification(new HeaderNotificationResponse(hasAlarm, hasChat));
        return emitter;
    }

    private void sendNotification(Object data) {
        taskExecutor.execute(() -> {
            SseEmitter.SseEventBuilder event = SseEmitter.event();
            event.name("header")
                    .data(data);
            try{
                emitter.send(event);
                emitter.complete();
                log.info("notification sent successfully");
            }catch (Exception e){
                emitter.completeWithError(e);
                log.error("notification sent failed");
            }
        });
    }

}
