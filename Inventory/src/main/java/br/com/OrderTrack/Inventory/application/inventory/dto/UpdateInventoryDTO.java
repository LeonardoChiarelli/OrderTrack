package br.com.OrderTrack.Inventory.application.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateInventoryDTO(

        @NotBlank
        String productIdOrNumber,

        @NotNull
        @Min(1)
        @Positive
        Integer quantity
) {
}
