package br.com.OrderTrack.Order.application.useCase;

import br.com.OrderTrack.Order.application.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.application.order.port.out.OrderRepositoryPort;
import br.com.OrderTrack.Order.domain.order.Order;
import br.com.OrderTrack.Order.domain.order.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class HandlePaymentApprovedUseCase {

    private final OrderRepositoryPort repository;

    public HandlePaymentApprovedUseCase(OrderRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.NEW && order.getStatus() != OrderStatus.PROCESSING) {
            return;
        }

        order.markAsProcessing();
        repository.save(order);

        // Opcional: Publicar evento na Outbox -> "OrderProcessingEvent" para o Track module
    }
}
