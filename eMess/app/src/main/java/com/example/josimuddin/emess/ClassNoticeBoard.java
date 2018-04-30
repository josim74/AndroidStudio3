package com.example.josimuddin.emess;

/**
 * Created by JosimUddin on 30/11/2017.
 */

public class ClassNoticeBoard {
    private String message;
    private String sender;
    private long time;

    public ClassNoticeBoard() {
    }

    public ClassNoticeBoard(String message, String sender, long time) {
        this.message = message;
        this.sender = sender;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public long getTime() {
        return time;
    }
}
