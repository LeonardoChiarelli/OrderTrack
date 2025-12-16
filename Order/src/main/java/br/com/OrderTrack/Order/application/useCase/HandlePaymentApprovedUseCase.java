package br.com.OrderTrack.Order.application.useCase;

import br.com.OrderTrack.Order.domain.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class HandlePaymentApprovedUseCase {

    private final OrderGateway repository;

    public HandlePaymentApprovedUseCase(OrderGateway repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(UUID orderId) {
        log.info("Processando aprovação do pagamento para o pedido {}", orderId);

        Order order = repository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            log.warn("Pedido {} não elegível para aprovação (Status: {})", orderId, order.getStatus());
            return;
        }

        order.markAsPaid();
        repository.save(order);

        log.info("Status do pedido {} atualizado para PROCESSING", orderId);

        // Publicar evento na Outbox -> "OrderProcessingEvent" para o Track module
    }
}
