package com.glim.chating.repository;

import com.glim.chating.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
