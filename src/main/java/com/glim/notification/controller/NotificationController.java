package com.glim.notification.controller;

import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.notification.domain.Type;
import com.glim.notification.service.HeaderNotificationService;
import com.glim.notification.service.NotificationService;
import com.glim.user.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final HeaderNotificationService headerNotificationService;

    @GetMapping({"/list","/list/{offset}"})
    public StatusResponseDTO list(@PathVariable(required = false) Long offset, @AuthenticationPrincipal SecurityUserDto user) {
        return StatusResponseDTO.ok(notificationService.getNotificationList(offset, user));
    }

    @GetMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(HttpServletResponse response, @AuthenticationPrincipal SecurityUserDto user) {
        return notificationService.getEmitter(response, user.getId());
    }

    @GetMapping(value = "/header", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter count(HttpServletResponse response, @AuthenticationPrincipal SecurityUserDto user) {
        return headerNotificationService.getEmitter(response, user.getId());
    }

    @PutMapping("/{id}")
    public StatusResponseDTO update(@PathVariable Long id){
        notificationService.updateRead(id);
        return StatusResponseDTO.ok("읽음 처리가 완료되었습니다.");
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable Long id) {
        notificationService.delete(id);
        return StatusResponseDTO.ok("알림이 삭제되었습니다.");
    }
}