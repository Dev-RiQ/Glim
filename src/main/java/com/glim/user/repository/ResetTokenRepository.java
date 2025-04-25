
package com.glim.user.repository;

import com.glim.user.domain.ResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResetTokenRepository extends MongoRepository<ResetToken, String> {
}
