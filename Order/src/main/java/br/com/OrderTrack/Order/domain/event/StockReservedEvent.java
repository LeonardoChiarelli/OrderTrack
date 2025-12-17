package br.com.OrderTrack.Order.domain.event;

import java.util.UUID;

public record StockReservedEvent(UUID orderId) {
}