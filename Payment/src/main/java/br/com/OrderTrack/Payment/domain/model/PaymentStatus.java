package br.com.OrderTrack.Payment.domain.model;

public enum PaymentStatus {
    PENDING,
    PROCESSING,
    AUTHORIZED,
    DECLINED,
    REFUNDED,
    CANCELLED
}
