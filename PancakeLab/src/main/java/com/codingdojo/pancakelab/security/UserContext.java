package com.codingdojo.pancakelab.security;

public class UserContext {

    private UserRole role;
    private String username;

    public UserContext(UserRole role, String username) {
        this.role = role;
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

}
