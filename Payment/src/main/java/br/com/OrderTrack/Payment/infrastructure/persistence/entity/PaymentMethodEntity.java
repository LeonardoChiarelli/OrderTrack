package br.com.OrderTrack.Payment.infrastructure.persistence.entity;

import br.com.OrderTrack.Payment.domain.exception.DomainException;
import br.com.OrderTrack.Payment.domain.model.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "PaymentMethod")
@Table(name = "payment_methods")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class PaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentEntity paymentId;

    public PaymentMethodEntity(PaymentMethod paymentMethod) {
        this.id = paymentMethod.getId();
        this.name = paymentMethod.getName();
        this.active = paymentMethod.isActive();
    }

    public void validateIsActive() {
        if (!this.active) {
            throw new DomainException(String.format("Payment method  %s is currently inactive.", this.name));
        }
    }
}
