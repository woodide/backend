package com.system.wood.domain.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send/{roomId}")
    public void send(@DestinationVariable String roomId,ChatMessage chatMessage) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        log.info(roomId,time);
        simpMessagingTemplate.convertAndSend("/assignment/" + roomId, new ChatMessage(chatMessage.getFrom(),chatMessage.getText(),chatMessage.getIsProfessor(), time));
    }
}
