package com.glim.rankingBoard.service;

import com.glim.borad.domain.BoardFiles;
import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.Boards;
import com.glim.borad.domain.FileType;
import com.glim.borad.repository.BoardFileRepository;
import com.glim.borad.repository.BoardRepository;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingBoardService {

    private final RankingBoardMongoRepository rankingBoardMongoRepository;
    private final BoardRepository boardRepository;
    private final AwsS3Util awsS3Util;
    private final BoardFileRepository boardFileRepository;

    @Cacheable(value = "rankingCache", key = "#period + '_' + #type.name() + '_' + #criteria", unless = "#period != 'realtime'")
    public List<RankingBoardResponse> getRankingList(String period, BoardType type, String criteria) {
        String finalPeriod = period + "_" + criteria;
        List<RankingBoardDocument> documents = rankingBoardMongoRepository.findByPeriodAndTypeOrderByViewCountDesc(finalPeriod, type.name());

        return documents.stream()
                .map(doc -> {
                    String contentImgUrl = getContentImageUrl(doc, type);
                    return RankingBoardResponse.from(doc, contentImgUrl);
                })
                .toList();
    }

    private String getContentImageUrl(RankingBoardDocument document, BoardType type) {
        if (type == BoardType.SHORTS) {
            if (document.getThumbnailUrl() != null) {
                return awsS3Util.getURL(document.getThumbnailUrl(), FileSize.IMAGE_128);
            } else {
                return null;
            }
        } else if (type == BoardType.BASIC) {
            List<BoardFiles> images = boardFileRepository.findByBoardIdAndFileTypeOrderByBoardFileIdAsc(document.getBoardId(), FileType.IMAGE);
            if (!images.isEmpty()) {
                String firstImageFileName = images.get(0).getFileName();
                return awsS3Util.getURL(firstImageFileName, FileSize.IMAGE_128);
            }
        }
        return null;
    }

    @Transactional
    public void saveRanking(String period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;
        LocalDateTime end;

        switch (period) {
            case "realtime" -> {
                start = now.minusMinutes(30);
                end = now;
            }
            case "day" -> {
                start = LocalDate.now().minusDays(1).atStartOfDay();
                end = LocalDate.now().minusDays(1).atTime(23, 59, 59);
            }
            case "week" -> {
                LocalDate lastWeekMonday = LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY);
                LocalDate lastWeekSunday = lastWeekMonday.plusDays(6);
                start = lastWeekMonday.atStartOfDay();
                end = lastWeekSunday.atTime(23, 59, 59);
            }
            case "month" -> {
                YearMonth lastMonth = YearMonth.now().minusMonths(1);
                start = lastMonth.atDay(1).atStartOfDay();
                end = lastMonth.atEndOfMonth().atTime(23, 59, 59);
            }
            case "year" -> {
                int lastYear = now.getYear() - 1;
                start = LocalDate.of(lastYear, 1, 1).atStartOfDay();
                end = LocalDate.of(lastYear, 12, 31).atTime(23, 59, 59);
            }
            default -> {
                start = now.minusMinutes(30);
                end = now;
            }
        }

        for (com.glim.borad.domain.BoardType boardType : List.of(
                com.glim.borad.domain.BoardType.BASIC,
                com.glim.borad.domain.BoardType.SHORTS)) {

            saveRankingByCriteria(period, boardType, start, end, "view");
            saveRankingByCriteria(period, boardType, start, end, "like");
        }

    }

    /**
     * 랭킹을 하나의 기준(view or like)에 맞춰 저장하는 메서드
     */
    private void saveRankingByCriteria(String period, BoardType boardType, LocalDateTime start, LocalDateTime now, String criteria) {
        List<Boards> boards;

        if ("view".equals(criteria)) {
            boards = boardRepository.findByCreatedAtBetweenAndBoardTypeOrderByViewsDescCreatedAtDesc(start, now, boardType, PageRequest.of(0, 20));
        } else if ("like".equals(criteria)) {
            boards = boardRepository.findByCreatedAtBetweenAndBoardTypeOrderByLikesDescCreatedAtDesc(start, now, boardType, PageRequest.of(0, 20));
        } else {
            throw new IllegalArgumentException("Invalid criteria: " + criteria);
        }

        List<RankingBoardDocument> rankingDocuments = boards.stream()
                .map(board -> RankingBoardDocument.builder()
                        .period(period + "_" + criteria)
                        .type(boardType.name())
                        .boardId(board.getId())
                        .title(board.getLocation())
                        .content(board.getContent())
                        .thumbnailUrl(null)
                        .viewCount(board.getViews())
                        .likeCount(board.getLikes())
                        .createdAt(board.getCreatedAt())
                        .build())
                .toList();

        rankingBoardMongoRepository.deleteByPeriodAndType(period + "_" + criteria, boardType.name());
        rankingBoardMongoRepository.saveAll(rankingDocuments);
    }


    @Scheduled(cron = "*/30 * * * * *")
    @CacheEvict(value = "rankingCache", allEntries = true)
    public void clearRankingCache() {
        System.out.println("랭킹 캐시 삭제 완료 30초 마다 갱신 확인용 로그");
    }
}
