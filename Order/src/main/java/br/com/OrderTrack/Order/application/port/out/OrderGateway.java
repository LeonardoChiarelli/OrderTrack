package br.com.OrderTrack.Order.application.port.out;

import br.com.OrderTrack.Order.domain.order.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderGateway {

    Order save(Order order);
    Optional<Order> findById(UUID id);
}
