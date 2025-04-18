package com.glim.notification.service;

import com.glim.borad.service.BoardService;
import com.glim.borad.service.BoardViewService;
import com.glim.notification.domain.Notification;
import com.glim.notification.domain.Type;
import com.glim.notification.dto.response.NotificationResponse;
import com.glim.notification.repository.NotificationRepository;
import com.glim.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 3600000L;
    private final UserService userService;
    private final BoardService boardService;
    private final NotificationRepository notificationRepository;
    private final ExecutorService taskExecutor = Executors.newSingleThreadExecutor();
    private SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    private Long viewId = 0L;

    public SseEmitter getEmitter(final HttpServletResponse response) {
        List<NotificationResponse> notifications = getNewNotification();
        if(!notifications.isEmpty()) {
            emitter = new SseEmitter(DEFAULT_TIMEOUT);
            viewId = notifications.get(notifications.size() - 1).getId();
        }
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        sendNotification(notifications);
        return emitter;
    }

    private List<NotificationResponse> getNewNotification() {
//        String nickname = SecurityUtil.getUserNickname();
//        Long userId = userService.findByNickname(nickname).getId();
        Long userId = 1L;
        List<NotificationResponse> notificationList = notificationRepository.findAllByUserIdAndIdGreaterThan(userId, viewId)
                .stream().map(NotificationResponse::new).collect(Collectors.toList());
        for (NotificationResponse notificationResponse : notificationList) {
//            String userImg = userService.findById(notificationResponse.getUserId());
//            notificationResponse.setUserImg(userImg);
            if (notificationResponse.getLinkId() != null && notificationResponse.getLinkId() != 0L) {
//                String linkImg = boardService.findById(notificationResponse.getLinkId());
//                notificationResponse.setLinkImg(linkImg);
            }
        }
        return notificationList;
    }

    private void sendNotification(Object data) {
        taskExecutor.execute(() -> {
            SseEmitter.SseEventBuilder event = SseEmitter.event();
            event.name("notification")
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

    @Transactional
    public void send(Long userId, String sendUserNickname, Type type){
        send(userId, sendUserNickname, type, 0L);
    }

    @Transactional
    public void send(Long userId, String sendUserNickname, Type type, Long linkId){
//        String nickname = SecurityUtil.getUserNickname();
//        Long sendUserId = userService.findByNickname(nickname).getId();
        Long sendUserId = 2L;
        Notification notification = new Notification(userId, sendUserId, sendUserNickname, type, linkId);
        notificationRepository.save(notification);
    }

    @Transactional
    public void delete(long id) {
        notificationRepository.deleteById(id);
    }
}
