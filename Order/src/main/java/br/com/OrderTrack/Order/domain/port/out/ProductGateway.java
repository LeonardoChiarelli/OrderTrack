package br.com.OrderTrack.Order.domain.port.out;

import br.com.OrderTrack.Order.domain.model.valueObject.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductGateway {

    Optional<Product> findById(UUID id);
}
