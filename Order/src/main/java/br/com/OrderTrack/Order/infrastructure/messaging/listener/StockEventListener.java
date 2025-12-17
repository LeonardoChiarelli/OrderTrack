package br.com.OrderTrack.Order.infrastructure.messaging.listener;

import br.com.OrderTrack.Order.domain.port.in.ApproveOrderStockInputPort;
import br.com.OrderTrack.Order.domain.port.in.CancelOrderInputPort;
import br.com.OrderTrack.Order.infrastructure.configuration.RabbitConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockEventListener {

    private final ApproveOrderStockInputPort approveOrderStockUseCase;
    private final CancelOrderInputPort cancelOrderUseCase;

    public record StockEventDTO(@JsonProperty("orderId") String orderId) {}

    @RabbitListener(queues = RabbitConfig.STOCK_RESERVED_QUEUE)
    public void handleStockReserved(StockEventDTO event) {
        try {
            log.info("Evento recebido (Estoque Reservado): {}", event.orderId());
            approveOrderStockUseCase.execute(UUID.fromString(event.orderId()));
        } catch (Exception e) {
            log.error("Erro crítico ao processar reserva de estoque", e);
            throw new AmqpRejectAndDontRequeueException("Erro no processamento de estoque", e);
        }
    }

    @RabbitListener(queues = RabbitConfig.STOCK_FAILED_QUEUE)
    public void handleStockFailed(StockEventDTO event) {
        try {
            log.info("Evento recebido (Falha no Estoque): {}", event.orderId());
            cancelOrderUseCase.execute(UUID.fromString(event.orderId()));
        } catch (Exception e) {
            log.error("Erro crítico ao processar falha de estoque", e);
            throw new AmqpRejectAndDontRequeueException("Erro no processamento de falha de estoque", e);
        }
    }
}