package com.glim.chating.repository;

import com.glim.chating.domain.ChatMsg;
import org.springframework.data.domain.Limit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMsgRepository extends MongoRepository<ChatMsg, Long> {
    List<ChatMsg> findAllByRoomIdOrderByMsgIdDesc(Long roomId, Limit limit);
    List<ChatMsg> findAllByRoomIdAndMsgIdLessThanOrderByMsgIdDesc(Long roomId, Long MsgId, Limit limit);
    Optional<ChatMsg> findTop1ByOrderByMsgIdDesc();
}
