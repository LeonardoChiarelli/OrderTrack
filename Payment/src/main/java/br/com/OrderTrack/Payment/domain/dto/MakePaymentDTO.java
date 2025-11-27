package br.com.OrderTrack.Payment.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MakePaymentDTO(
        Long id,

        @NotBlank
        String cardholderName,

        @NotBlank
        @Pattern(regexp = "\\d{4} \\d{4} \\d{4} \\d{4}")
        String cardNumber,

        @NotBlank
        @Pattern(regexp = "\\d{2}/\\d{2}")
        String expirationDate,

        @NotBlank
        @Size(min = 3, max = 3)
        @Pattern(regexp = "\\d{3}")
        String securityCode,

        @NotNull
        Long paymentMethodId
) {
}
