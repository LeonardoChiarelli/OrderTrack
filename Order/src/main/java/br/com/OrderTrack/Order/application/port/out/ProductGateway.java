package br.com.OrderTrack.Order.application.port.out;

import br.com.OrderTrack.Order.domain.product.Product;

import java.util.Optional;

public interface ProductGateway {

    Optional<Product> findByName(String name);
}
