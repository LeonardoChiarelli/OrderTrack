package br.com.OrderTrack.Order.infrastructure.order.persistence.mapper;

import br.com.OrderTrack.Order.domain.order.valueObject.Address;
import br.com.OrderTrack.Order.infrastructure.order.persistence.entity.valueObject.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressEntityMapper {

    public AddressEntity toEntity(Address address) {
        return new AddressEntity(address);
    }

    public Address toDomain(AddressEntity addressEntity) {
        if (addressEntity.getComplement().isBlank()) {
            return Address.builder()
                    .street(addressEntity.getStreet())
                    .neighborhood(addressEntity.getNeighborhood())
                    .postalCode(addressEntity.getPostalCode())
                    .city(addressEntity.getCity())
                    .number(addressEntity.getNumber())
                .build();
        }

        return Address.builder()
                .street(addressEntity.getStreet())
                .neighborhood(addressEntity.getNeighborhood())
                .postalCode(addressEntity.getPostalCode())
                .city(addressEntity.getCity())
                .number(addressEntity.getNumber())
                .complement(addressEntity.getComplement())
            .build();
    }
}
