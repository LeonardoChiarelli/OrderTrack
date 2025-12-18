package br.com.OrderTrack.Order.application.useCase;

import br.com.OrderTrack.Order.domain.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateOrderStatusUseCase {
    private final OrderGateway repository;

    @Transactional
    public void execute(UUID orderId, OrderStatus newStatus) {
        var order = repository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        switch (newStatus) {
            case PREPARING -> order.markAsPreparing();
            case SHIPPED -> order.markAsShipped();
            case DELIVERED -> order.markAsDelivered();
            default -> throw new IllegalArgumentException(String.format("Invalid order status: %s", newStatus));
        }

        repository.save(order);
        log.info("Pedido {} atualizado para {}", orderId, newStatus);
    }
}
