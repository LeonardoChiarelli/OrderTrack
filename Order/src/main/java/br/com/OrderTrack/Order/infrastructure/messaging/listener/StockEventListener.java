package br.com.OrderTrack.Order.infrastructure.messaging.listener;

import br.com.OrderTrack.Order.domain.event.PaymentRequestedEvent;
import br.com.OrderTrack.Order.domain.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import br.com.OrderTrack.Order.infrastructure.messaging.adapter.RabbitEventPublisherAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockEventListener {

    private final OrderGateway orderGateway;
    private final RabbitEventPublisherAdapter eventPublisher;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "stock.reserved.order.queue")
    @Transactional
    public void handleStockReserved(String payload) {
        try {
            log.info("Estoque reservado: {}", payload);
            JsonNode node = objectMapper.readTree(payload);
            UUID orderId = UUID.fromString(node.get("orderId").asText());

            Order order = orderGateway.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));

            if (order.getStatus() != OrderStatus.PENDING_STOCK) {
                log.warn("Pedido {} em estado inválido para iniciar pagamento: {}", orderId, order.getStatus());
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

        } catch (Exception e) {
            log.error("Erro ao processar reserva de estoque", e);
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = "stock.failed.order.queue")
    @Transactional
    public void handleStockFailed(String payload) {
        try {
            log.info("Falha na reserva de estoque: {}", payload);
            JsonNode node = objectMapper.readTree(payload);
            UUID orderId = UUID.fromString(node.get("orderId").asText());

            Order order = orderGateway.findById(orderId).orElse(null);
            if (order != null) {
                order.markAsCanceled();
                orderGateway.save(order);
                log.info("Pedido {} cancelado por falta de estoque.", orderId);
            }
        } catch (Exception e) {
            log.error("Erro ao processar falha de estoque", e);
        }
    }
}
