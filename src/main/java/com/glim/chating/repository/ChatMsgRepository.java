package com.glim.chating.repository;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMsgRepository extends JpaRepository<ChatMsg, Long> {
}
