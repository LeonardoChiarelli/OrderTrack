package br.com.OrderTrack.Order.application.user.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInDTO(
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
