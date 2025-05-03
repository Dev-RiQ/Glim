package com.glim.notification.service;

import com.glim.borad.repository.BoardFileRepository;
import com.glim.borad.repository.BoardRepository;
import com.glim.borad.service.BoardFileSevice;
import com.glim.borad.service.BoardService;
import com.glim.borad.service.BoardViewService;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.common.utils.SecurityUtil;
import com.glim.notification.domain.Notification;
import com.glim.notification.domain.Type;
import com.glim.notification.dto.response.NotificationResponse;
import com.glim.notification.repository.NotificationRepository;
import com.glim.stories.repository.StoryRepository;
import com.glim.stories.service.StoryService;
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
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final BoardFileRepository boardFileRepository;
    private final AwsS3Util awsS3Util;
    private final NotificationRepository notificationRepository;
    private final ExecutorService taskExecutor = Executors.newSingleThreadExecutor();
    private final UserService userService;
    private SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);


    public List<NotificationResponse> getNotificationList(Long offset) {
        User user = userRepository.findById(SecurityUtil.getUser().getId()).orElseThrow(ErrorCode::throwDummyNotFound);
        List<Notification> list = offset == null ? notificationRepository.findAllByUserIdAndIdLessThanOrderByIdDesc(user.getId(),user.getReadAlarmId(), Limit.of(30))
                :notificationRepository.findAllByUserIdAndIdLessThanAndIdLessThanOrderByIdDesc(user.getId(),user.getReadAlarmId(), offset, Limit.of(30));
        List<NotificationResponse> notificationList = list.stream().map(NotificationResponse::new).collect(Collectors.toList());
        setImg(notificationList, user);
        return notificationList;
    }

    public SseEmitter getEmitter(final HttpServletResponse response, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(ErrorCode::throwDummyNotFound);
        List<NotificationResponse> notifications = getNewNotification(user);
        if(!notifications.isEmpty()) {
            emitter = new SseEmitter(DEFAULT_TIMEOUT);
            userService.updateReadAlarmId(user.getId(), notifications.get(notifications.size() - 1).getId());
        }
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        sendNotification(notifications);
        return emitter;
    }

    private List<NotificationResponse> getNewNotification(User user) {
        List<NotificationResponse> notificationList = notificationRepository.findAllByUserIdAndIdGreaterThan(user.getId(), user.getReadAlarmId()).orElseThrow(ErrorCode::throwDummyNotFound)
                .stream().map(NotificationResponse::new).collect(Collectors.toList());
        setImg(notificationList, user);
        return notificationList;
    }

    private void setImg(List<NotificationResponse> notificationList, User user) {
        for (NotificationResponse notificationResponse : notificationList) {
            notificationResponse.setUserImg(awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128));
            if (notificationResponse.getLinkId() != null && notificationResponse.getLinkId() != 0L) {
                String linkImg = null;
                switch (notificationResponse.getUri().substring(0, notificationResponse.getUri().lastIndexOf("/") + 1)){
                    case "/board/" -> linkImg = awsS3Util.getURL(boardFileRepository.findById(notificationResponse.getLinkId())
                            .orElseThrow(ErrorCode::throwDummyNotFound).getFileName(), FileSize.IMAGE_128);
                    case "/shorts/" -> linkImg = awsS3Util.getURL(boardFileRepository.findById(notificationResponse.getLinkId())
                            .orElseThrow(ErrorCode::throwDummyNotFound).getFileName(), FileSize.VIDEO_THUMBNAIL);
                    case "/story/" -> linkImg = awsS3Util.getURL(storyRepository.findById(notificationResponse.getLinkId())
                            .orElseThrow(ErrorCode::throwDummyNotFound).getFileName(), FileSize.IMAGE_128);
                    case "/user/" -> linkImg = awsS3Util.getURL(userRepository.findById(notificationResponse.getLinkId())
                            .orElseThrow(ErrorCode::throwDummyNotFound).getImg(), FileSize.IMAGE_128);
                }
                notificationResponse.setLinkImg(linkImg);
            }
        }
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
        SecurityUserDto user = SecurityUtil.getUser();
        Notification notification = new Notification(userId, user.getId(), sendUserNickname, type, linkId);
        notificationRepository.save(notification);
    }

    @Transactional
    public void delete(long id) {
        notificationRepository.deleteById(id);
    }

    @Transactional
    public void deleteNotificationsByUser(Long userId) {
        notificationRepository.deleteByUserIdOrSendUserId(userId, userId);
    }

    @Transactional
    public void updateRead(Long id){
        Notification notification = notificationRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        if(!notification.getIsRead()) {
            notification.update();
            notificationRepository.save(notification);
        }
    }

}
