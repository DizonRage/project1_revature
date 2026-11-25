package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email) {
}
