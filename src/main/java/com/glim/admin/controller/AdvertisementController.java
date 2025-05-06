package com.glim.admin.controller;

import com.glim.admin.domain.Advertisement;
import com.glim.admin.domain.AdvertisementStatus;
import com.glim.admin.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ad") // 모든 광고 관련 요청은 "/ad"로 시작
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final Random random = new Random();

    //    전체 광고 목록 조회
    @GetMapping("/list")
    public List<Advertisement> getAllAdvertisements() {
        return advertisementService.findAll();
    }

    //     특정 광고 하나 조회
    @GetMapping("/{id}")
    public Advertisement findRandomAdvertisement(@PathVariable String id) {
        List<Advertisement> approvedAds = advertisementService.findByStatus();

        if (approvedAds.isEmpty()) {
            throw new IllegalStateException("승인된 광고가 존재하지 않습니다.");
        }

        int randomIndex = random.nextInt(approvedAds.size()); // 랜덤 인덱스 선택
        return approvedAds.get(randomIndex);
    }

    //     광고 승인 처리
    @PostMapping("/approve/{id}")
    public Advertisement approveAdvertisement(@PathVariable Long id) {
        return advertisementService.approve(id);
    }

    //     광고 거절 처리
    @PostMapping("/reject/{id}")
    public Advertisement rejectAdvertisement(@PathVariable Long id) {
        return advertisementService.reject(id);
    }
}
