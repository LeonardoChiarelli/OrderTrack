package br.com.OrderTrack.Order.infrastructure.messaging.listener;

import br.com.OrderTrack.Order.domain.port.in.ApproveOrderStockInputPort;
import br.com.OrderTrack.Order.domain.port.in.CancelOrderInputPort;
import br.com.OrderTrack.Order.infrastructure.configuration.RabbitConfig;
import br.com.OrderTrack.Order.infrastructure.messaging.service.IdempotencyService;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.ProcessedEventEntity;
import br.com.OrderTrack.Order.infrastructure.persistence.repository.JPAProcessedEventsRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IdempotencyService idempotencyService;

    public record StockEventDTO(@JsonProperty("orderId") String orderId) {}

    @RabbitListener(queues = RabbitConfig.STOCK_RESERVED_QUEUE)
    @Transactional
    public void handleStockReserved(StockEventDTO event, @Header(name = "X-Correlation-ID", required = false) String messageId) {
        String key = (messageId != null) ? messageId : "STOCK-RES-" + event.orderId();

        idempotencyService.process(key, event, (e) -> {
            log.info("Evento recebido (Estoque Reservado): {}", e.orderId());
            approveOrderStockUseCase.execute(UUID.fromString(e.orderId()));
        });
    }

    @RabbitListener(queues = RabbitConfig.STOCK_FAILED_QUEUE)
    public void handleStockFailed(StockEventDTO event, @Header(name = "X-Correlation-ID", required = false) String messageId) {
        String key = (messageId != null) ? messageId : "STOCK-FAIL-" + event.orderId();

        idempotencyService.process(key, event, (e) -> {
            log.info("Evento recebido (Falha no Estoque): {}", e.orderId());
            cancelOrderUseCase.execute(UUID.fromString(e.orderId()));
        });
    }
}