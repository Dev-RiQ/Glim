package com.glim.notification.dto.response;

import com.glim.common.utils.DateTimeUtil;
import com.glim.notification.domain.Notification;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationResponse {
    private Long id;
    private Long userId;
    private Long linkId;
    private String message;
    private String uri;
    private String createdAt;
    private String userImg;
    private String linkImg;

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.userId = notification.getUserId();
        this.linkId = notification.getLinkId();
        this.message = notification.getMessage();
        this.uri = notification.getUri() + notification.getLinkId();
        this.createdAt = DateTimeUtil.getDateTimeAgo(notification.getCreatedAt());
    }
}
