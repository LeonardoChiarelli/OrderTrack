package br.com.OrderTrack.Order.domain.event;

import java.util.List;
import java.util.UUID;

public record OrderCancelledEvent(UUID orderId, List<OrderItemEventDTO> items) {
    public record OrderItemEventDTO(UUID productId, Integer quantity) {}
}
