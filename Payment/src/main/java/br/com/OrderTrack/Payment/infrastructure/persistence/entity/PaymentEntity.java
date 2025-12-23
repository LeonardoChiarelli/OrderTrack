package br.com.OrderTrack.Payment.infrastructure.persistence.entity;

import br.com.OrderTrack.Payment.domain.exception.DomainException;
import br.com.OrderTrack.Payment.domain.model.Payment;
import br.com.OrderTrack.Payment.domain.model.PaymentDetails;
import br.com.OrderTrack.Payment.domain.model.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Payment")
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id", "orderId"})
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID orderId;

    private BigDecimal amount;

    @OneToMany(cascade = CascadeType.ALL)
    private PaymentMethodEntity paymentMethodId;
    private PaymentDetails paymentDetails;
    private PaymentStatus status;
    private LocalDateTime transactionDate;
    private String transactionId;

    public PaymentEntity(Payment payment, PaymentMethodEntity paymentMethodEntity) {
        this.id = payment.getId();
        this.orderId = payment.getOrderId();
        this.amount = payment.getAmount();
        this.paymentMethodId = paymentMethodEntity;
        this.paymentDetails = payment.getPaymentDetails();
        this.status = payment.getStatus();
        this.transactionDate = payment.getTransactionDate();
        this.transactionId = payment.getTransactionId();
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
}
