package com.glim.rankingBoard.controller;

import com.glim.rankingBoard.dto.request.RankingBoardRequest;
import com.glim.rankingBoard.dto.response.RankingBoardResponse;
import com.glim.rankingBoard.service.RankingBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        rankingBoardService.saveRanking("day");
        return ResponseEntity.ok("랭킹 저장 완료!");
    }
}
