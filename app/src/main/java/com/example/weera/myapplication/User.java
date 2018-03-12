package com.example.weera.myapplication;

/**
 * Created by weera on 12/03/2018.
 */
public class User {

    public String image;
    public String timestamp;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String image, String timestamp) {
        this.image = image;
        this.timestamp = timestamp;
    }

}