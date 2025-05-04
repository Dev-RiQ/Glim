package com.glim.rankingBoard.repository;

import com.glim.rankingBoard.domain.RankingBoardDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface RankingBoardMongoRepository extends MongoRepository<RankingBoardDocument, String> {
    Optional<List<RankingBoardDocument>> findByPeriodAndTypeOrderByViewCountDesc(String period, String type);
    void deleteByPeriodAndType(String period, String type);
}
