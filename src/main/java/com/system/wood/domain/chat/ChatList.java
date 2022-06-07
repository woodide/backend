package com.system.wood.domain.chat;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ChatList {

    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    @NotNull
    private String roomId;

    @NotNull
    private String sender;

    @NotNull
    private String text;

    @NotNull
    private String send_time;

    @NotNull
    private boolean isProfessor;

    @Builder
    public ChatList(String roomId, String sender, String text, String send_time, boolean isProfessor) {
        this.roomId = roomId;
        this.sender = sender;
        this.text = text;
        this.send_time = send_time;
        this.isProfessor = isProfessor;
    }

    public static ChatList of(String roomId, String sender, String text, String send_time, boolean isProfessor) {
        return ChatList.builder()
                .roomId(roomId).sender(sender).text(text).send_time(send_time).isProfessor(isProfessor)
                .build();
    }
}
