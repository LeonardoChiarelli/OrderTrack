package br.com.OrderTrack.Order.domain.event;

import br.com.OrderTrack.Common.exception.ValidationException;
import br.com.OrderTrack.Order.domain.exception.DomainException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class PaymentRequestedEvent {
    private final UUID orderId;
    private final BigDecimal totalPrice;
    private final String currency;

    private PaymentRequestedEvent(UUID orderId,
                                  BigDecimal totalPrice,
                                  String currency) {
        if (orderId == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0 || currency.isBlank()) {
            throw new DomainException("All Payment Requested Event must be provided.");
        }

        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.currency = currency;
    }

    public static PaymentRequestedEventBuilder builder() {
        return new PaymentRequestedEventBuilder();
    }

    public UUID getOrderId() {
        return orderId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentRequestedEvent that = (PaymentRequestedEvent) o;
        return Objects.equals(getOrderId(), that.getOrderId()) && Objects.equals(getTotalPrice(), that.getTotalPrice()) && Objects.equals(getCurrency(), that.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getTotalPrice(), getCurrency());
    }

    @Override
    public String toString() {
        return "PaymentRequestedEvent{" +
                "orderId=" + orderId +
                ", totalPrice=" + totalPrice +
                ", currency='" + currency + '\'' +
                '}';
    }

    public static class PaymentRequestedEventBuilder {
        private UUID orderId;
        private BigDecimal totalPrice;
        private String currency;

        public PaymentRequestedEventBuilder() {}

        public PaymentRequestedEventBuilder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public PaymentRequestedEventBuilder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public PaymentRequestedEventBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public PaymentRequestedEvent build() {
            if (orderId == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0 || currency.isBlank()) {
                throw new ValidationException("All Payment Requested Event must be provided.");
            }

            return new PaymentRequestedEvent(
                    this.orderId,
                    this.totalPrice,
                    this.currency
            );
        }
    }
}
