package com.example.api.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    // Getters and Setters
    private String token;
    private User user;

    public LoginResponse() {
    }

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

}
