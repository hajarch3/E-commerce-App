package com.example.e_commerce_app;

public class User {
    private String uid;
    private String email;
    private String username;
    private String isAdmin;

    public User(String uid, String email, String username, String isAdmin) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }
    public String getUsername() {
        return username; }
    public String getIsAdmin() {
        return isAdmin; }
}
