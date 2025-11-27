package br.com.OrderTrack.Payment.domain.model;

import br.com.OrderTrack.Payment.domain.dto.MakePaymentDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalPrice;
    private String cardholderName;
    private String cardNumber;
    private String expirationDate;
    private String securityCode;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paymentDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    public Payment(Order order, @Valid MakePaymentDTO dtoPayment, PaymentMethod method) {
        this.totalPrice = order.getTotalPrice();
        this.cardholderName = dtoPayment.cardholderName();
        this.cardNumber = dtoPayment.cardNumber();
        this.expirationDate = dtoPayment.expirationDate();
        this.securityCode = dtoPayment.securityCode();
        this.status = PaymentStatus.PROCESSING;
        this.paymentDate = order.getOrderDate();
        this.order = order;
        this.paymentMethod = method;
    }

    public void changeStatus(PaymentStatus paymentStatus) {
        this.status = paymentStatus;
        this.paymentDate = LocalDateTime.now();
    }
}
