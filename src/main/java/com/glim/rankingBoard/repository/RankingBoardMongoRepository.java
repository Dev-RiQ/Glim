package com.glim.rankingBoard.repository;

import com.glim.rankingBoard.domain.RankingBoardDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RankingBoardMongoRepository extends MongoRepository<RankingBoardDocument, String> {
    List<RankingBoardDocument> findByPeriodAndTypeOrderByViewCountDesc(String period, String type);
    void deleteByPeriodAndType(String period, String type);
}
