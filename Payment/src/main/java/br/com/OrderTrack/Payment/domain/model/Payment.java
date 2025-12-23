package br.com.OrderTrack.Payment.domain.model;

import br.com.OrderTrack.Payment.domain.exception.DomainException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Payment {
    private final UUID id;
    private final UUID orderId;
    private final BigDecimal amount;
    private final PaymentMethod paymentMethod;
    private final PaymentDetails paymentDetails;
    private PaymentStatus status;
    private LocalDateTime transactionDate;
    private String transactionId;

    private Payment(UUID orderId, BigDecimal amount, PaymentMethod paymentMethod, PaymentDetails paymentDetails) {
        if (orderId == null) throw new DomainException("Order ID is required.");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new DomainException("Invalid payment amount.");
        if (paymentMethod == null) throw new DomainException("Payment method is required.");
        if (paymentDetails == null) throw new DomainException("Payment details is required.");

        this.id = UUID.randomUUID();
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
        this.status = PaymentStatus.PENDING;
        this.transactionDate = LocalDateTime.now();
    }

    private Payment(UUID id, UUID orderId, BigDecimal amount, PaymentMethod paymentMethod, PaymentDetails paymentDetails) {
        if (orderId == null) throw new DomainException("Order ID is required.");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new DomainException("Invalid payment amount.");
        if (paymentMethod == null) throw new DomainException("Payment method is required.");
        if (paymentDetails == null) throw new DomainException("Payment details is required.");

        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
        this.status = PaymentStatus.PENDING;
        this.transactionDate = LocalDateTime.now();
    }

    public static PaymentBuilder builder() {
        return new PaymentBuilder();
    }

    public void authorize(String externalTransactionId) {
        if (this.status != PaymentStatus.PENDING && this.status != PaymentStatus.PROCESSING) {
            throw new DomainException("Payment cannot be approved from status: " + this.status);
        }
        this.status = PaymentStatus.AUTHORIZED;
        this.transactionId = externalTransactionId;
        this.transactionDate = LocalDateTime.now();
    }

    public void decline(String reason) {
        this.status = PaymentStatus.DECLINED;
        this.transactionDate = LocalDateTime.now();
    }

    public void refund(String externalTransactionId) {
        if (this.status != PaymentStatus.PENDING && this.status != PaymentStatus.PROCESSING) {
            throw new DomainException("Payment cannot be approved from status: " + this.status);
        }
        this.status = PaymentStatus.REFUNDED;
        this.transactionId = externalTransactionId;
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELLED;
        this.transactionDate = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(getId(), payment.getId()) && Objects.equals(getOrderId(), payment.getOrderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrderId());
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", paymentMethod=" + paymentMethod +
                ", paymentDetails=" + paymentDetails +
                ", status=" + status +
                ", transactionDate=" + transactionDate +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }

    public static class PaymentBuilder {
        private UUID id;
        private UUID orderId;
        private BigDecimal amount;
        private PaymentMethod paymentMethod;
        private PaymentDetails paymentDetails;

        public PaymentBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public PaymentBuilder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public PaymentBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public PaymentBuilder paymentMethod(PaymentMethod paymentMethod) {
            paymentMethod.validateIsActive();
            this.paymentMethod = paymentMethod;
            return this;
        }

        public PaymentBuilder paymentDetails(PaymentDetails paymentDetails) {
            this.paymentDetails = paymentDetails;
            return this;
        }

        public Payment  build() {
            if (id != null) {
                return new Payment(id, orderId, amount, paymentMethod, paymentDetails);
            }
            return new Payment(orderId, amount, paymentMethod, paymentDetails);
        }
    }
}
