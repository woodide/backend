package com.system.wood.domain.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatApiController {
    private final ChatService chatService;

    @GetMapping("/chat/{roomId}")
    public List<ChatList> getChat(@PathVariable String roomId) {
        return chatService.getChatByRoomId(roomId);
    }
}
