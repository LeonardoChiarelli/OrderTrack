package br.com.OrderTrack.Order.infrastructure.messaging.listener;

import br.com.OrderTrack.Order.domain.port.in.ApproveOrderStockInputPort;
import br.com.OrderTrack.Order.domain.port.in.CancelOrderInputPort;
import br.com.OrderTrack.Order.infrastructure.configuration.RabbitConfig;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.ProcessedEventEntity;
import br.com.OrderTrack.Order.infrastructure.persistence.repository.JPAProcessedEventsRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockEventListener {

    private final ApproveOrderStockInputPort approveOrderStockUseCase;
    private final CancelOrderInputPort cancelOrderUseCase;
    private final JPAProcessedEventsRepository processedEventsRepository;

    public record StockEventDTO(@JsonProperty("orderId") String orderId) {}

    @RabbitListener(queues = RabbitConfig.STOCK_RESERVED_QUEUE)
    @Transactional
    public void handleStockReserved(StockEventDTO event, @Header(name = "X-Correlation-ID", required = false) String messageId) {
        String idempotencyKey = (messageId != null) ? messageId : "STOCK-RES-" + event.orderId();

        if (processedEventsRepository.existsById(idempotencyKey)) {
            log.warn("Evento de Estoque Reservado duplicado ignorado. Key: {}", idempotencyKey);
            return;
        }

        log.info("Evento recebido (Estoque Reservado): {}", event.orderId());
        approveOrderStockUseCase.execute(UUID.fromString(event.orderId()));

        processedEventsRepository.save(new ProcessedEventEntity(idempotencyKey, LocalDateTime.now()));
    }

    @RabbitListener(queues = RabbitConfig.STOCK_FAILED_QUEUE)
    public void handleStockFailed(StockEventDTO event, @Header(name = "X-Correlation-ID", required = false) String messageId) {
        String idempotencyKey = (messageId != null) ? messageId : "STOCK-FAIL-" + event.orderId();

        if (processedEventsRepository.existsById(idempotencyKey)) {
            log.warn("Evento de Falha de Estoque duplicado ignorado. Key: {}", idempotencyKey);
            return;
        }

        log.info("Evento recebido (Falha no Estoque): {}", event.orderId());
        cancelOrderUseCase.execute(UUID.fromString(event.orderId()));

        processedEventsRepository.save(new ProcessedEventEntity(idempotencyKey, LocalDateTime.now()));
    }
}