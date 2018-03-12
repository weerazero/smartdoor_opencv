package com.example.weera.myapplication;

/**
 * Created by weera on 08/03/2018.
 */

public class Upload {
    public Upload( ) {

    }
    public Upload(String image, String timestamp) {
        this.image = image;
        this.timestamp = timestamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String image;
    private String timestamp;

}