package br.com.OrderTrack.Order.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderedItemsDTO(
        Long id,

        @NotBlank
        List<String> productsName,

        @NotNull
        Integer quantity
) {
}
