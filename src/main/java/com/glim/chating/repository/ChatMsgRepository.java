package com.glim.chating.repository;

import com.glim.chating.domain.ChatMsg;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMsgRepository extends MongoRepository<ChatMsg, Long> {
}
