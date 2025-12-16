package br.com.OrderTrack.Order.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderedItemsDTO(
        Long id,

        @NotBlank
        UUID productId,

        @NotNull
        @Positive
        Integer quantity
) {
}
