package com.example.summar.model;

public class User {
    private String email;
    private String password;
    private String name;
    private String gender;
    private String birth;

    public User(String email, String password, String name, String gender, String birth){
        this.email=email;
        this.password=password;
        this.name=name;
        this.gender=gender;
        this.birth=birth;
    }
}
