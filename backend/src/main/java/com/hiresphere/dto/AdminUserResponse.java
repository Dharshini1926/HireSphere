package com.hiresphere.dto;

import com.hiresphere.entity.User;

public class AdminUserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String role;
    private String createdAt;

    public AdminUserResponse(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.createdAt = user.getCreatedAt().toString();
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}