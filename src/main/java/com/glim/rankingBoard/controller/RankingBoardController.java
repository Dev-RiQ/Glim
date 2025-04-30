package com.glim.rankingBoard.controller;

import com.glim.rankingBoard.dto.request.RankingBoardRequest;
import com.glim.rankingBoard.dto.response.RankingBoardResponse;
import com.glim.rankingBoard.service.RankingBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ranking-board")
@RequiredArgsConstructor
public class RankingBoardController {

    private final RankingBoardService rankingBoardService;

    @PostMapping("/list")
    public ResponseEntity<List<RankingBoardResponse>> getRankingList(@RequestBody RankingBoardRequest request) {
        List<RankingBoardResponse> rankings = rankingBoardService.getRankingList(
                request.getPeriod(),
                request.getType(),
                request.getCriteria()
        );
        return ResponseEntity.ok(rankings);
    }

    @PostMapping("/test-save")
    public ResponseEntity<String> testSaveRanking() {
        rankingBoardService.saveRanking("year");
        return ResponseEntity.ok("랭킹 저장 완료!");
    }

    // 30초마다 실시간 랭킹 저장
    @Scheduled(cron = "*/30 * * * * *")
    public void saveRealtimeRanking() {
        System.out.println("[SCHEDULE] ⏱ 실시간 랭킹 저장 실행됨");
        rankingBoardService.saveRanking("realtime");
    }

    // 매일 오전 00:10 → 어제 랭킹 저장
    @Scheduled(cron = "0 10 0 * * *")
    public void saveDailyRanking() {
        rankingBoardService.saveRanking("day");
    }

    // 매주 월요일 오전 00:20 → 지난주 랭킹 저장
    @Scheduled(cron = "0 20 0 * * MON")
    public void saveWeeklyRanking() {
        rankingBoardService.saveRanking("week");
    }

    // 매월 1일 오전 00:30 → 지난달 랭킹 저장
    @Scheduled(cron = "0 30 0 1 * *")
    public void saveMonthlyRanking() {
        rankingBoardService.saveRanking("month");
    }

    // 매년 1월 1일 오전 00:40 → 작년 랭킹 저장
    @Scheduled(cron = "0 40 0 1 1 *")
    public void saveYearlyRanking() {
        rankingBoardService.saveRanking("year");
    }
}
