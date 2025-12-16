package br.com.OrderTrack.Order.application.useCase;

import br.com.OrderTrack.Order.domain.event.OrderCancelledEvent;
import br.com.OrderTrack.Order.domain.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import br.com.OrderTrack.Order.domain.port.in.CancelOrderInputPort;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import br.com.OrderTrack.Order.infrastructure.messaging.adapter.RabbitEventPublisherAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelOrderUseCase implements CancelOrderInputPort {

    private final OrderGateway repository;
    private final RabbitEventPublisherAdapter eventPublisher;

    @Override
    @Transactional
    public void execute(UUID orderId) {
        log.info("Iniciando cancelamento do pedido: {}", orderId);

        Order order = repository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for ID: " + orderId));

        if (order.getStatus() == OrderStatus.CANCELED) {
            log.warn("Pedido {} já está cancelado.", orderId);
            return;
        }

        order.markAsCanceled();
        repository.save(order);

        List<OrderCancelledEvent.OrderItemEventDTO> items = order.getItems().stream()
                .map(i -> new OrderCancelledEvent.OrderItemEventDTO(i.getProduct().getId(), i.getQuantity()))
                .toList();

        OrderCancelledEvent event = new OrderCancelledEvent(orderId, items);

        eventPublisher.publish(event, "OrderCancelledEvent", orderId.toString());

        log.info("Pedido {} cancelado com sucesso. Evento de compensação disparado.", orderId);
    }
}