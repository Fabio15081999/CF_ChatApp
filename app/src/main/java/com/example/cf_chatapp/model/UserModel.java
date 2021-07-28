package com.example.cf_chatapp.model;

public class UserModel {

    private String id;
    private String username;
    private String avatar;
    private String email;


    public UserModel(String id, String username, String avatar, String email) {
        this.id = id;
        this.username = username;

        this.avatar = avatar;
        this.email = email;
    }

    public UserModel() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}