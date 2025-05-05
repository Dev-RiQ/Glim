package com.glim.chating.repository;

import com.glim.chating.domain.ChatMsg;
import org.springframework.data.domain.Limit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMsgRepository extends MongoRepository<ChatMsg, Long> {
    List<ChatMsg> findAllByRoomIdOrderByMsgIdDesc(Long roomId, Limit limit);
    Optional<ChatMsg> findTop1ByOrderByMsgIdDesc();
    Optional<ChatMsg> findTop1ByRoomIdOrderByMsgIdDesc(Long roomId);
    Optional<List<ChatMsg>> findAllByRoomIdAndMsgIdGreaterThanOrderByMsgIdDesc(Long roomId, Long loadMin, Limit of);
    Optional<List<ChatMsg>> findAllByRoomIdAndMsgIdBetweenOrderByMsgIdDesc(Long roomId, Long loadMin, Long offset, Limit of);
    void deleteAllByRoomId(Long roomId);
}
