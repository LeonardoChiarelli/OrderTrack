package br.com.OrderTrack.Track.domain.model;

import br.com.OrderTrack.Track.domain.dto.AddressDTO;
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
public class Address {
    public String street;
    public String neighborhood;
    public String postalCode;
    public String city;
    public String state;
    public String number;
    public String complement;

    public Address(@NotNull @Valid AddressDTO addressDTO) {
        this.street = addressDTO.street();
        this.neighborhood = addressDTO.neighborhood();
        this.postalCode = addressDTO.postalCode();
        this.city = addressDTO.city();
        this.state = addressDTO.state();
        this.number = addressDTO.number();
        this.complement = addressDTO.complement();
    }
}
