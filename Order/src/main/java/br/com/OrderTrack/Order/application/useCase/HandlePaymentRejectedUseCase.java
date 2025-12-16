package br.com.OrderTrack.Order.application.useCase;

import br.com.OrderTrack.Order.domain.event.OrderCancelledEvent;
import br.com.OrderTrack.Order.domain.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import br.com.OrderTrack.Order.infrastructure.messaging.adapter.RabbitEventPublisherAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandlePaymentRejectedUseCase {
    private final OrderGateway repository;
    private final RabbitEventPublisherAdapter eventPublisher;

    @Transactional
    public void execute(UUID orderId) {
        log.info("Processando rejeição de pagamento para o pedido: {}", orderId);

        Order order = repository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for ID: " + orderId));

        if (order.getStatus() == OrderStatus.CANCELED) {
            log.warn("Pedido {} já está cancelado. Ignorando evento.", orderId);
            return;
        }

        order.markAsCanceled();
        repository.save(order);

        OrderCancelledEvent event = new OrderCancelledEvent(orderId, order.getItems());
        eventPublisher.publish(event, "OrderCancelledEvent", orderId.toString());

        log.info("Order {} canceled. OrderCancelledEvent published for compensation.", orderId);
    }
}
