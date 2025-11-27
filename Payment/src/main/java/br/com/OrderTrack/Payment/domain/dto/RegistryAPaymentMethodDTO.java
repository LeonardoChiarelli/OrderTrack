package br.com.OrderTrack.Payment.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistryAPaymentMethodDTO(
        Long id,

        @NotBlank
        String name
) {
}
