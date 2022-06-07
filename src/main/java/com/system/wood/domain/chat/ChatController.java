package com.system.wood.domain.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    @MessageMapping("/send/{roomId}")
    public void send(@DestinationVariable String roomId,ChatMessage chatMessage) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        log.info(roomId,time);
        ChatList chat = ChatList.of(roomId, chatMessage.getFrom(),chatMessage.getText(),time ,chatMessage.getIsProfessor());
        chatService.save(chat);
        simpMessagingTemplate.convertAndSend("/assignment/" + roomId, new ChatMessage(chatMessage.getFrom(),chatMessage.getText(),chatMessage.getIsProfessor(), time));
    }

}
