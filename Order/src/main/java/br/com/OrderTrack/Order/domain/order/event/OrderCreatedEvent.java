package br.com.OrderTrack.Order.domain.order.event;

import java.util.UUID;

public record OrderCreatedEvent(UUID id) {
}
