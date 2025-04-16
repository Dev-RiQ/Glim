
package com.glim.chating.repository;

import com.glim.chating.domain.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    Optional<List<ChatUser>> findAllByRoomId(Long roomId);
}
