package br.com.OrderTrack.Order.domain.event;

import br.com.OrderTrack.Order.domain.model.OrderStatus;
import java.util.UUID;

public record OrderStatusChangedEvent(
        UUID orderId,
        OrderStatus newStatus,
        String consumerEmail
) {
}