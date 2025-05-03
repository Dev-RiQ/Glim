package com.glim.notification.dto.response;

import com.glim.common.utils.DateTimeUtil;
import com.glim.notification.domain.Notification;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
public class HeaderNotificationResponse {
    private Boolean hasAlarm;
    private Boolean hasChat;
}
