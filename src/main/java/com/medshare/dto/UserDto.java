package com.medshare.dto;

import com.medshare.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDto {
    private UUID userId;
    private String email;
    private String fullName;
    private String phone;
    private User.UserType userType;
    private Double locationLat;
    private Double locationLng;
    private String address;
    private Boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    // Constructors
    public UserDto() {}

    public UserDto(UUID userId, String email, String fullName, String phone, User.UserType userType,
                   Double locationLat, Double locationLng, String address, Boolean verified,
                   LocalDateTime createdAt, LocalDateTime lastLogin) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.userType = userType;
        this.locationLat = locationLat;
        this.locationLng = locationLng;
        this.address = address;
        this.verified = verified;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(Double locationLng) {
        this.locationLng = locationLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}