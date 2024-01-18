package com.example.androidchat.model;

public class User {
    private String full_name;
    private int user_id;
    private String email;
    private String password;
    public int getUser_id()
    {
        return user_id;
    }
    public String getEmail()
    {
        return email;
    }
    public String getPassword()
    {
        return password;
    }
    public String getFull_name()
    {
        return full_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
