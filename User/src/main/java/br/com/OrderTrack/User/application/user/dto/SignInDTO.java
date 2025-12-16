package br.com.OrderTrack.User.application.user.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInDTO(
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
