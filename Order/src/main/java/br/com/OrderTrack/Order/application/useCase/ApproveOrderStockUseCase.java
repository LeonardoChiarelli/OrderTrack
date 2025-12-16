package br.com.OrderTrack.Order.application.useCase;

import br.com.OrderTrack.Order.domain.event.PaymentRequestedEvent;
import br.com.OrderTrack.Order.domain.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import br.com.OrderTrack.Order.domain.port.in.ApproveOrderStockInputPort;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import br.com.OrderTrack.Order.infrastructure.messaging.adapter.RabbitEventPublisherAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApproveOrderStockUseCase implements ApproveOrderStockInputPort {

    private final OrderGateway orderGateway;
    private final RabbitEventPublisherAdapter eventPublisher;

    @Override
    @Transactional
    public void execute(UUID orderId) {
        log.info("Processando aprovação de estoque para o pedido: {}", orderId);

        Order order = orderGateway.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING_STOCK) {
            log.warn("Pedido {} em estado inválido para iniciar pagamento: {}. Ignorando.", orderId, order.getStatus());
            return;
        }

        order.markAsPendingPayment();
        orderGateway.save(order);

        var paymentRequest = PaymentRequestedEvent.builder()
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .currency("BRL")
                .build();

        eventPublisher.publish(paymentRequest, "PaymentRequestedEvent", order.getId().toString());
        log.info("Pedido {} aprovado no estoque. Solicitando pagamento.", orderId);
    }
}