package com.example.weera.myapplication;

/**
 * Created by weera on 08/03/2018.
 */

class FriendlyMessage {
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String from;
    private String name;
    private String timestamp;
    public FriendlyMessage(String from, String name, String timestamp) {
        this.from = from;
        this.name = name;
        this.timestamp = timestamp;
    }




}
