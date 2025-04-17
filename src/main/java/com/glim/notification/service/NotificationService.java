package com.glim.notification.service;

import com.glim.common.utils.SecurityUtil;
import com.glim.notification.domain.Notification;
import com.glim.notification.repository.NotificationRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L;
    private NotificationRepository notificationRepository;
    private final ExecutorService taskExecutor = Executors.newSingleThreadExecutor();
    private SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

    public SseEmitter getEmitter(final HttpServletResponse response) {
        List<Notification> notifications = getNewNotification();
        if(!notifications.isEmpty()) {
            emitter = new SseEmitter(DEFAULT_TIMEOUT);
        }
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        sendNotification(notifications);
        return emitter;
    }

    private List<Notification> getNewNotification() {
//        Long userId = SecurityUtil.getUser().getId();
        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(new Notification(1L, "test"));
        // 유저 아이디 알람에서 최신 알람 있는지 검색 (역순 마지막 번호가 보유한 번호보다 큰가?)
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
                log.info("notification sent failed");
            }
        });
    }


}
