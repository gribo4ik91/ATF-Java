package com.example.api.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    // Getters and Setters
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }


}
