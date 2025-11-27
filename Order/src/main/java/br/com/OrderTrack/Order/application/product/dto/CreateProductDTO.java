package br.com.OrderTrack.Order.application.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateProductDTO(
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotBlank
        String category,

        @NotNull
        BigDecimal price,

        @NotNull
        @Positive
        @Min(1)
        Integer initialInventory
) {
}
