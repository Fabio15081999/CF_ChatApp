package com.example.cf_chatapp.model;

public class UserModel {
    private String email;
    private String passWord;


    public UserModel(String email, String passWord) {
        this.email = email;
        this.passWord = passWord;
    }

    public UserModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
