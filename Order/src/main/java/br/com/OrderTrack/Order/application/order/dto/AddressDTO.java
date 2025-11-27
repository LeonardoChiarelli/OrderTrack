package br.com.OrderTrack.Order.application.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressDTO(

        @NotBlank
        String street,

        @NotBlank
        String neighborhood,

        @Pattern(regexp = "\\d{8}")
        @NotBlank
        String postalCode,

        @NotBlank
        String city,

        @NotBlank
        @Size(max = 2)
        String state,

        @NotBlank
        @Pattern(regexp = "\\d")
        String number,

        String complement
) {
}
