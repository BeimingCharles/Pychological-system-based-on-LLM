package com.example.test.my_tools;

public class Message {
    public  static  String SENT_BY_ME = "user";
    public  static String SENT_BY_BOT="assistant";
    String message;
    String sentBY;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBY() {
        return sentBY;
    }

    public void setSentBY(String sentBY) {
        this.sentBY = sentBY;
    }

    public Message(String message, String sentBY) {
        this.message = message;
        this.sentBY = sentBY;
    }
}
