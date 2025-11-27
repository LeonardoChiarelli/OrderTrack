package br.com.OrderTrack.Payment.domain.model;

public enum OrderStatus {
    NEW,
    PROCESSING,
    PACKAGE,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELED
}
