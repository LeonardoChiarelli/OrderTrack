package br.com.OrderTrack.Payment.domain.model;

import br.com.OrderTrack.Payment.domain.dto.RegistryAPaymentMethodDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_methods")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean active;

    public PaymentMethod(@Valid RegistryAPaymentMethodDTO dto) {
        this.name = dto.name();
        this.active = true;
    }

    public void changeStatus(boolean status) {
        this.active = status;
    }
}
