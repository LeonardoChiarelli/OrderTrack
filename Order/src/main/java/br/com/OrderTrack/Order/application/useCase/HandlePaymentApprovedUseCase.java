package br.com.OrderTrack.Order.application.useCase;

import br.com.OrderTrack.Order.domain.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import br.com.OrderTrack.Order.infrastructure.messaging.adapter.RabbitEventPublisherAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class HandlePaymentApprovedUseCase {

    private final OrderGateway repository;
    private final RabbitEventPublisherAdapter eventPublisher;

    public HandlePaymentApprovedUseCase(OrderGateway repository, RabbitEventPublisherAdapter eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void execute(UUID orderId) {
        log.info("Processando aprovação do pagamento para o pedido {}", orderId);

        Order order = repository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.PREPARING) {
            log.warn("Pedido {} já foi processado anteriormente.", orderId);
            return;
        }

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            log.warn("Pedido {} não elegível para aprovação (Status: {}). Esperado: PENDING_PAYMENT", orderId, order.getStatus());
            return;
        }

        order.markAsPaid();
        repository.save(order);

        log.info("Status do pedido {} atualizado para PAID", orderId);

        eventPublisher.publish(new OrderPaidEvent(orderId), "OrderPaidEvent", orderId.toString());
    }

    public record OrderPaidEvent(UUID orderId) {}
}

