package com.glim.rankingBoard.service;

import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.Boards;
import com.glim.borad.repository.BoardRepository;
import com.glim.rankingBoard.domain.RankingBoardDocument;
import com.glim.rankingBoard.dto.response.RankingBoardResponse;
import com.glim.rankingBoard.repository.RankingBoardMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingBoardService {

    private final RankingBoardMongoRepository rankingBoardMongoRepository;
    private final BoardRepository boardRepository;

    @Cacheable(value = "rankingCache", key = "#period + '_' + #type + '_' + #criteria", unless = "#period != 'realtime'")
    public List<RankingBoardResponse> getRankingList(String period, String type, String criteria) {
        String finalPeriod = period + "_" + criteria;
        List<RankingBoardDocument> documents = rankingBoardMongoRepository.findByPeriodAndTypeOrderByViewCountDesc(finalPeriod, type);
        return documents.stream()
                .map(RankingBoardResponse::from)
                .toList();
    }

    @Transactional
    public void saveRanking(String period) {
        for (BoardType boardType : BoardType.values()) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime start = switch (period) {
                case "day" -> now.minusDays(1);
                case "week" -> now.minusWeeks(1);
                case "month" -> now.minusMonths(1);
                case "year" -> now.minusYears(1);
                default -> now.minusMinutes(30);
            };

            List<Boards> topBoardsByView = boardRepository.findTopBoardsByPeriodAndType(start, now, boardType.name(), PageRequest.of(0, 20));
            List<RankingBoardDocument> viewRankings = topBoardsByView.stream()
                    .map(board -> RankingBoardDocument.builder()
                            .period(period + "_view")
                            .type(boardType.name())
                            .boardId(board.getId())
                            .title(board.getLocation())  // ✅ getLocation() 사용
                            .content(board.getContent())
                            .thumbnailUrl(null)           // ✅ 썸네일 없음
                            .viewCount(board.getViews())  // ✅ getViews() 사용
                            .likeCount(board.getLikes())  // ✅ getLikes() 사용
                            .createdDate(board.getCreatedAt()) // ✅ getCreatedAt() 사용
                            .build())
                    .toList();
            rankingBoardMongoRepository.deleteByPeriodAndType(period + "_view", boardType.name());
            rankingBoardMongoRepository.saveAll(viewRankings);

            List<Boards> topBoardsByLike = boardRepository.findTopBoardsByLikesAndType(start, now, boardType.name(), PageRequest.of(0, 20));
            List<RankingBoardDocument> likeRankings = topBoardsByLike.stream()
                    .map(board -> RankingBoardDocument.builder()
                            .period(period + "_like")
                            .type(boardType.name())
                            .boardId(board.getId())
                            .title(board.getLocation())  // ✅ getLocation() 사용
                            .content(board.getContent())
                            .thumbnailUrl(null)           // ✅ 썸네일 없음
                            .viewCount(board.getViews())  // ✅ getViews() 사용
                            .likeCount(board.getLikes())  // ✅ getLikes() 사용
                            .createdDate(board.getCreatedAt()) // ✅ getCreatedAt() 사용
                            .build())
                    .toList();
            rankingBoardMongoRepository.deleteByPeriodAndType(period + "_like", boardType.name());
            rankingBoardMongoRepository.saveAll(likeRankings);
        }
    }

    @Scheduled(cron = "*/30 * * * * *")
    @CacheEvict(value = "rankingCache", allEntries = true)
    public void clearRankingCache() {
        System.out.println("⏰ 랭킹 캐시 삭제 완료!");
    }
}
