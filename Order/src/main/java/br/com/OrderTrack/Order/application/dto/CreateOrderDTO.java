package br.com.OrderTrack.Order.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderDTO(

        @NotNull
        @Valid
        AddressDTO shippingAddress,

        @NotNull
        @Valid
        List<OrderedItemsDTO> items
) {
}
