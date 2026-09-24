package com.eldercare.models;

import java.time.LocalDateTime;

public class User {

    private int userId;
    private String username;
    private String passwordHash;
    private String role;
    private boolean isActive;
    private LocalDateTime createdAt;

    // Constructor for creating a new user
    public User(String username, String passwordHash, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for loading a user from the database
    public User(int userId, String username, String passwordHash,
                String role, boolean isActive,
                LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isValid() {
        return username != null && !username.isEmpty()
                && passwordHash != null && !passwordHash.isEmpty()
                && role != null && !role.isEmpty();
    }
}