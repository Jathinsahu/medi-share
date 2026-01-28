package com.medshare.dto;

import com.medshare.model.User;

public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private String message;
    private Long userId;
    private User.UserType userType;

    // Constructors
    public AuthResponse() {}

    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public AuthResponse(String token) {
        this.token = token;
    }

    public AuthResponse(String token, String message, Long userId, User.UserType userType) {
        this.token = token;
        this.message = message;
        this.userId = userId;
        this.userType = userType;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }
}