package org.example.dto;

import jakarta.validation.constraints.NotBlank;

public record AddOwnerRequest(@NotBlank String username) {
}
