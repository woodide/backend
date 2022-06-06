package com.system.wood.domain.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send/{imageName}")
    public void send(@DestinationVariable String imageName,ChatMessage chatMessage) throws Exception {
      //  String time = new SimpleDateFormat("HH:mm").format(new Date());
        log.info(imageName);
        simpMessagingTemplate.convertAndSend("/assignment/" + imageName,chatMessage);
    }
}
