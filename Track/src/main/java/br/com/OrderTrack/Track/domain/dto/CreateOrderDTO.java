package br.com.OrderTrack.Track.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderDTO(
        Long id,

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
