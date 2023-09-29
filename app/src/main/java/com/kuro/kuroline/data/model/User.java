package com.kuro.kuroline.data.model;

public class User {
    private String uid;
    private String username;
    private String userProfilePicture;
    private String email;
    private String password;
    private String status;
    private String phoneNumber;
    private String lastMessage;

    // Contacts
    // allMessages

    public User (){

    }

    public User(String uid, String username, String userProfilePicture, String email, String password, String status, String phoneNumber, String lastMessage) {
        this.uid = uid;
        this.username = username;
        this.userProfilePicture = userProfilePicture;
        this.email = email;
        this.password = password;
        this.status = status;
        this.phoneNumber = phoneNumber;
        this.lastMessage = lastMessage;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
