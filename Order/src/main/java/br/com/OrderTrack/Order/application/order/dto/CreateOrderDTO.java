package br.com.OrderTrack.Order.application.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderDTO(
        UUID id,

        @NotBlank
        String consumerName,

        @NotBlank
        @Email
        String consumerEmail,

        @NotNull
        @Valid
        AddressDTO shippingAddress,

        @NotNull
        @Valid
        List<OrderedItemsDTO> items
) {
}
