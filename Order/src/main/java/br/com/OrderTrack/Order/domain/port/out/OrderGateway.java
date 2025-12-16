package br.com.OrderTrack.Order.domain.port.out;

import br.com.OrderTrack.Order.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderGateway {

    Order save(Order order);
    Optional<Order> findById(UUID id);
}
