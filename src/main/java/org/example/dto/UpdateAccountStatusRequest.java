package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateAccountStatusRequest(
        @NotBlank @Pattern(regexp = "CANCELLED", message = "must be CANCELLED") String status) {
}
