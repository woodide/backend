package com.system.wood.domain.chat;

public class ChatMessage {
    private String from;
    private String text;
    private String time;

    private boolean isProfessor;

    public ChatMessage(String from, String text,boolean isProfessor, String time) {
        this.from = from;
        this.text = text;
        this.time = time;
        this.isProfessor = isProfessor;
    }

    public String getFrom() {
        return this.from;
    }


    public boolean getIsProfessor() {
        return this.isProfessor;
    }

    public String getText() {
        return this.text;
    }

    public String getTime() {
        return this.time;
    }
}