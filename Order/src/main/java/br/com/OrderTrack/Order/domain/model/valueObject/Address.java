package br.com.OrderTrack.Order.domain.model.valueObject;

import br.com.OrderTrack.Common.exception.ValidationException;
import br.com.OrderTrack.Order.domain.exception.DomainException;

public class Address {
    private final String street;
    private final String neighborhood;
    private final String postalCode;
    private final String city;
    private final String state;
    private final String number;
    private String complement;

    private Address(
            String street,
            String neighborhood,
            String postalCode,
            String city,
            String state,
            String number,
            String complement
    ) {
        if (street.isBlank() || neighborhood.isBlank() || postalCode.isBlank() || city.isBlank() || state.isBlank() || number.isBlank()) {
            throw new DomainException("All address core must be provided.");
        }

         this.street = street;
         this.neighborhood = neighborhood;
         this.postalCode = postalCode;
         this.city = city;
         this.state = state;
         this.number = number;
         this.complement = complement;
    }

    private Address(
            String street,
            String neighborhood,
            String postalCode,
            String city,
            String state,
            String number
    ) {
        if (street.isBlank() || neighborhood.isBlank() || postalCode.isBlank() || city.isBlank() || state.isBlank() || number.isBlank()) {
            throw new DomainException("All address core must be provided.");
        }

        this.street = street;
        this.neighborhood = neighborhood;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.number = number;
    }

    public static AddressBuilder builder() {
        return new AddressBuilder();
    }

    public String getStreet() {
        return street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getNumber() {
        return number;
    }

    public String getComplement() {
        return complement;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", number='" + number + '\'' +
                ", complement='" + complement + '\'' +
                '}';
    }

    public static class AddressBuilder {
        private String street;
        private String neighborhood;
        private String postalCode;
        private String city;
        private String state;
        private String number;
        private String complement;

        public AddressBuilder street(String street) {
            this.street = street;
            return this;
        }

        public AddressBuilder neighborhood(String neighborhood) {
            this.neighborhood = neighborhood;
            return this;
        }

        public AddressBuilder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public AddressBuilder city(String city) {
            this.city = city;
            return this;
        }

        public AddressBuilder state(String state) {
            this.state = state;
            return this;
        }

        public AddressBuilder number(String number) {
            this.number = number;
            return this;
        }

        public AddressBuilder complement(String complement) {
            this.complement = complement;
            return this;
        }

        public Address build() {
            if (street.isBlank() || neighborhood.isBlank() || postalCode.isBlank() || city.isBlank() || state.isBlank() || number.isBlank()) {
                throw new ValidationException("All address core must be provided.");
            }

            if (complement != null) {
                return new Address(street,
                        neighborhood,
                        postalCode,
                        city,
                        state,
                        number,
                        complement
                );
            }

            return new Address(
                    street,
                    neighborhood,
                    postalCode,
                    city,
                    state,
                    number
            );
        }
    }
}
