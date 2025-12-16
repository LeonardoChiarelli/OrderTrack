package br.com.OrderTrack.Order.infrastructure.persistence.entity.valueObject;

import br.com.OrderTrack.Order.application.dto.AddressDTO;
import br.com.OrderTrack.Order.domain.model.valueObject.Address;
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

    public AddressEntity(Address shippingAddress) {
        this.street = shippingAddress.getStreet();
        this.neighborhood = shippingAddress.getNeighborhood();
        this.postalCode = shippingAddress.getPostalCode();
        this.city = shippingAddress.getCity();
        this.state = shippingAddress.getState();
        this.number = shippingAddress.getNumber();
        this.complement = shippingAddress.getComplement();
    }
}
