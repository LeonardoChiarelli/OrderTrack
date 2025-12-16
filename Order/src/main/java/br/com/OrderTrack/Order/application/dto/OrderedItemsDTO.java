package br.com.OrderTrack.Order.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderedItemsDTO(
        Long id,

        @NotBlank
        String productName,

        @NotNull
        @Positive
        Integer quantity
) {
}
