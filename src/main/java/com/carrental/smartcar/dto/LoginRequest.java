package com.carrental.smartcar.dto;

public class LoginRequest {
    private String username;
    private String password;
    private String email;

    // Getters and setters (or use Lombok)
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
