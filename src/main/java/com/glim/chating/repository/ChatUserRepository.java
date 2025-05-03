
package com.glim.chating.repository;

import com.glim.chating.domain.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    Optional<ChatUser> findByRoomIdAndUserIdNot(Long roomId, Long userId);
    Optional<ChatUser> findByRoomIdAndUserId(Long roomId, Long userId);
    Optional<List<ChatUser>> findAllByUserId(Long userId);
    void deleteByUserId(Long userId);

    boolean existsByRoomIdAndUserId(Long roomId, Long id);
}
