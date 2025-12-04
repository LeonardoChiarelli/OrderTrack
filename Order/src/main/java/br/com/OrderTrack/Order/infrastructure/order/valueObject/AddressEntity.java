package br.com.OrderTrack.Order.infrastructure.order.valueObject;

import br.com.OrderTrack.Order.application.order.dto.AddressDTO;
import jakarta.persistence.Embeddable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddressEntity {
    private String street;
    private String neighborhood;
    private String postalCode;
    private String city;
    private String state;
    private String number;
    private String complement;

    public AddressEntity(@NotNull @Valid AddressDTO addressDTO) {
        this.street = addressDTO.street();
        this.neighborhood = addressDTO.neighborhood();
        this.postalCode = addressDTO.postalCode();
        this.city = addressDTO.city();
        this.state = addressDTO.state();
        this.number = addressDTO.number();
        this.complement = addressDTO.complement();
    }
}
