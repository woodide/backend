package com.system.wood.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatList, Long> {
    List<ChatList> findByRoomId(String roomId);
}
