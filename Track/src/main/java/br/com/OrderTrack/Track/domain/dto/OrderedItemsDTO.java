package br.com.OrderTrack.Track.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderedItemsDTO(
        Long id,

        @NotBlank
        String productName,

        @NotNull
        Integer quantity,

        @NotNull
        BigDecimal unitPrice
) {
}
