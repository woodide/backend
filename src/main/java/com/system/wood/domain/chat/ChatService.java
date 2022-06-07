package com.system.wood.domain.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public List<ChatList> getChatByRoomId(String roomId) {
        return chatRepository.findByRoomId(roomId);
    }

    @Transactional
    public ChatList save(ChatList chat) {
        ChatList savedChat = chatRepository.save(chat);
        return savedChat;
    }
}
