package br.com.OrderTrack.Payment.domain.model;

import jakarta.persistence.Embeddable;
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

}
