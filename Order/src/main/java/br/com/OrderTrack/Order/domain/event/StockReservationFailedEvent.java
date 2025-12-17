package br.com.OrderTrack.Order.domain.event;

import java.util.UUID;

public record StockReservationFailedEvent(UUID orderId, String reason) {
}