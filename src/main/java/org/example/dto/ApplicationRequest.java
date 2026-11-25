package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ApplicationRequest(
        @NotBlank @Pattern(regexp = "CHECKING|SAVINGS", message = "must be CHECKING or SAVINGS") String accountType) {
}
