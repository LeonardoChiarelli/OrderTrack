package br.com.OrderTrack.Order.application.order.port.out;

import br.com.OrderTrack.Order.domain.order.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(UUID id);
}
