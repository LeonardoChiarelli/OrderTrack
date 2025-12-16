package br.com.OrderTrack.Order.domain.port.out;

import br.com.OrderTrack.Product.domain.product.Product;

import java.util.Optional;

public interface ProductGateway {

    Optional<Product> findByName(String name);
}
