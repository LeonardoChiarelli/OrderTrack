package br.com.OrderTrack.Order.application.order.dto;

import br.com.OrderTrack.Order.infrastructure.order.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeOrderStatus(
        @NotNull
        Long id,

        @NotNull
        OrderStatus status
) {
}
