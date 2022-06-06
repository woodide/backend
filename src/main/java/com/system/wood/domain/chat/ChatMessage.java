package com.system.wood.domain.chat;

public class ChatMessage {
    private String from;
    private String text;
    private String time;

    public ChatMessage(String from, String text,String time) {
        this.from = from;
        this.text = text;
        this.time = time;
    }

    public String getFrom() {
        return this.from;
    }

    public String getText() {
        return this.text;
    }

    public String getTime() {
        return this.time;
    }
}