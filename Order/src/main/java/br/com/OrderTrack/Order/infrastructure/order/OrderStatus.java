package br.com.OrderTrack.Order.infrastructure.order;

public enum OrderStatus {
    NEW,
    PROCESSING,
    PACKAGE,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELED
}
