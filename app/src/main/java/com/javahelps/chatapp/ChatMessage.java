package com.javahelps.chatapp;

import java.util.Date;

/**
 * Created by Sneha on 04-03-2018.
 */

public class ChatMessage {
    private String messageText ;
    private String messageUser ;
    private long messagetime ;

    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        messagetime = new Date().getTime() ;
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessagetime() {
        return messagetime;
    }

    public void setMessagetime(long messagetime) {
        this.messagetime = messagetime;
    }
}
