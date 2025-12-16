package br.com.OrderTrack.Order.domain.port.out;

import br.com.OrderTrack.Order.domain.model.valueObject.Product;

import java.util.Optional;

public interface ProductGateway {

    Optional<Product> findByName(String name);
}
