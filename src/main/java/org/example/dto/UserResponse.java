package org.example.dto;

import org.example.entity.User;

public record UserResponse(
        Integer userId,
        String username,
        String firstName,
        String lastName,
        String email,
        String role) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole());
    }
}
