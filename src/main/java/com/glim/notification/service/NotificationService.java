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
    private SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);


    public List<NotificationResponse> getNotificationList(Long offset, SecurityUserDto me) {
        User user = userRepository.findById(me.getId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Notification> list = offset == null ? notificationRepository.findAllByUserIdAndIdLessThanOrderByIdDesc(user.getId(),user.getReadAlarmId() + 1, Limit.of(30))
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND))
                :notificationRepository.findAllByUserIdAndIdLessThanAndIdLessThanOrderByIdDesc(user.getId(),user.getReadAlarmId() + 1, offset, Limit.of(30))
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NO_MORE));
        List<NotificationResponse> notificationList = list.stream().map(NotificationResponse::new).collect(Collectors.toList());
        setImg(notificationList, user);
        return notificationList;
    }

    @Transactional
    public SseEmitter getEmitter(final HttpServletResponse response, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<NotificationResponse> notifications = getNewNotification(user);
        if(!notifications.isEmpty()) {
            emitter = new SseEmitter(DEFAULT_TIMEOUT);
            user.updateReadAlarmId(notifications.get(notifications.size() - 1).getId()); // 새 필드 업데이트
            userRepository.save(user);
        }
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        sendNotification(notifications);
        return emitter;
    }

    private List<NotificationResponse> getNewNotification(User user) {
        List<NotificationResponse> notificationList = notificationRepository.findAllByUserIdAndIdGreaterThan(user.getId(), user.getReadAlarmId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NO_CREATED))
                .stream().map(NotificationResponse::new).collect(Collectors.toList());
        setImg(notificationList, user);
        return notificationList;
    }

    private void setImg(List<NotificationResponse> notificationList, User user) {
        for (NotificationResponse notificationResponse : notificationList) {
            String userImg = userRepository.findById(notificationResponse.getSendUserId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)).getImg();
            notificationResponse.setUserImg(awsS3Util.getURL(userImg, FileSize.IMAGE_128));
            if (notificationResponse.getLinkId() != null && notificationResponse.getLinkId() != 0L) {
                String linkImg = null;
                switch (notificationResponse.getUri().substring(0, notificationResponse.getUri().lastIndexOf("/") + 1)){
                    case "/board/" -> linkImg = awsS3Util.getURL(boardFileRepository.findByBoardId(notificationResponse.getLinkId() ,Limit.of(1))
                            .orElseThrow(() -> new CustomException(ErrorCode.BOARDFILE_NOT_FOUND)).getFileName(), FileSize.IMAGE_128);
                    case "/shorts/" -> linkImg = awsS3Util.getURL(boardFileRepository.findByBoardId(notificationResponse.getLinkId())
                            .orElseThrow(() -> new CustomException(ErrorCode.BOARDFILE_NOT_FOUND)).getFileName(), FileSize.VIDEO_THUMBNAIL);
                    case "/story/" -> linkImg = awsS3Util.getURL(storyRepository.findById(notificationResponse.getLinkId())
                            .orElseThrow(() -> new CustomException(ErrorCode.BOARDFILE_NOT_FOUND)).getFileName(), FileSize.IMAGE_128);
                    case "/user/" -> linkImg = awsS3Util.getURL(userRepository.findById(notificationResponse.getLinkId())
                            .orElseThrow(() -> new CustomException(ErrorCode.BOARDFILE_NOT_FOUND)).getImg(), FileSize.IMAGE_128);
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
            }
        });
    }

    @Transactional
    public void send(Long userId, Type type, SecurityUserDto user){
        send(userId, type, 0L, user);
    }

    @Transactional
    public void send(Long userId, Type type, Long linkId, SecurityUserDto user){
        Notification notification = new Notification(userId, user.getId(), user.getNickname(), type, linkId);
        notificationRepository.save(notification);
    }

    @Transactional
    public void delete(Long id) {
        notificationRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.DUPLICATE_NOTIFICATION_DELETE));
        notificationRepository.deleteById(id);
    }

    @Transactional
    public void delete(Long userId, Type type, Long linkId, SecurityUserDto user) {
        if(type.toString().contains("COMMENT")){
            return;
        }
        Notification notification = notificationRepository.findByUserIdAndSendUserIdAndLinkIdAndType(userId, user.getId(), linkId, type)
                .orElse(null);
        if(notification != null) {
            notificationRepository.delete(notification);
        }
    }

    @Transactional
    public void deleteNotificationsByUser(Long userId) {
        notificationRepository.deleteByUserIdOrSendUserId(userId, userId);
    }

    @Transactional
    public void updateRead(Long id){
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
        if(!notification.getIsRead()) {
            notification.update();
            notificationRepository.save(notification);
        }
    }
}
