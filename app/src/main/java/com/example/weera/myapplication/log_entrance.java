package com.example.weera.myapplication;

/**
 * Created by weera on 04/03/2018.
 */

public class log_entrance{

private String from;
private String name;
private String timestamp;


    public log_entrance() {

    }

    public log_entrance(String from, String name, String timestamp, String key) {
        this.from = from;
        this.name = name;
        this.timestamp = timestamp;
    }

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
}
