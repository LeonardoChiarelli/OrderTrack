package br.com.OrderTrack.Order.domain.model;

public enum OrderStatus {
    PENDING_STOCK,
    PENDING_PAYMENT,
    PAID,
    PREPARING,
    SHIPPED,
    DELIVERED,
    CANCELED
}
