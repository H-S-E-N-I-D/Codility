package com.codingdojo.pancakelab.security;

public class SecurityManager {

    public void checkPermission(UserContext user, UserRole requiredRole) {
        if (user == null || user.getRole() != requiredRole) {
            throw new SecurityException("Unauthorized access");
        }
    }

}
