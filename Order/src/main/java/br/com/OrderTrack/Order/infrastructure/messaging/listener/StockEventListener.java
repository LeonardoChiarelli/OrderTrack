package br.com.OrderTrack.Order.infrastructure.messaging.listener;

import br.com.OrderTrack.Order.domain.port.in.ApproveOrderStockInputPort;
import br.com.OrderTrack.Order.domain.port.in.CancelOrderInputPort;
import br.com.OrderTrack.Order.infrastructure.configuration.RabbitConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockEventListener {

    private final ApproveOrderStockInputPort approveOrderStockUseCase;
    private final CancelOrderInputPort cancelOrderUseCase;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitConfig.STOCK_RESERVED_QUEUE)
    public void handleStockReserved(String payload) {
        try {
            log.info("Evento recebido (Estoque Reservado): {}", payload);
            JsonNode node = objectMapper.readTree(payload);

            if (!node.has("orderId")) {
                log.error("Payload inválido: orderId ausente (handleStockReserved)");
                return;
            }

            UUID orderId = UUID.fromString(node.get("orderId").asText());
            approveOrderStockUseCase.execute(orderId);

        } catch (Exception e) {
            log.error("Erro crítico ao processar reserva de estoque", e);
            // Em produção: lançar exceção para DLQ ou retentar
        }
    }

    @RabbitListener(queues = RabbitConfig.STOCK_FAILED_QUEUE)
    public void handleStockFailed(String payload) {
        try {
            log.info("Evento recebido (Falha no Estoque): {}", payload);
            JsonNode node = objectMapper.readTree(payload);

            if (!node.has("orderId")) {
                log.error("Payload inválido: orderId ausente (handleStockFailed)");
                return;
            }

            UUID orderId = UUID.fromString(node.get("orderId").asText());

            cancelOrderUseCase.execute(orderId);

        } catch (Exception e) {
            log.error("Erro crítico ao processar falha de estoque", e);
        }
    }
}