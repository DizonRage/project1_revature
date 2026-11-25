package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateApplicationStatusRequest(
        @NotBlank @Pattern(regexp = "APPROVED|DENIED", message = "must be APPROVED or DENIED") String status) {
}
