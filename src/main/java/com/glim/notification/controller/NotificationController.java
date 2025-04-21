package com.glim.notification.controller;

import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.notification.domain.Type;
import com.glim.notification.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(HttpServletResponse response) {
        return notificationService.getEmitter(response);
    }

    @DeleteMapping("/{id}")
    public StatusResponseDTO delete(@PathVariable long id) {
        notificationService.delete(id);
        return StatusResponseDTO.ok();
    }

    @PostMapping("/test")
    public StatusResponseDTO sendTest(){
        notificationService.send(1L, "Test", Type.BOARD_LIKE);
        notificationService.send(1L, "Test", Type.BOARD_LIKE, 1L);
        return StatusResponseDTO.ok();
    }
}