package br.com.OrderTrack.Order.infrastructure.messaging.listener;

import br.com.OrderTrack.Order.application.useCase.HandlePaymentApprovedUseCase;
import br.com.OrderTrack.Order.application.useCase.HandlePaymentRejectedUseCase;
import br.com.OrderTrack.Order.infrastructure.configuration.RabbitConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class PaymentEventListener {

    @Autowired
    private HandlePaymentApprovedUseCase approvedUseCase;

    @Autowired
    private HandlePaymentRejectedUseCase rejectedUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitConfig.PAYMENT_APPROVED_QUEUE)
    public void handlePaymentApproved(String payload) {
        try {
            log.info("Recebendo evento de Pagamento Aprovado: {}", payload);
            JsonNode node = objectMapper.readTree(payload);

            if (!node.has("orderId")) {
                log.error("Payload inválido: orderId ausente");
                throw new AmqpRejectAndDontRequeueException("Invalid Payload");
            }

            String orderIdStr = node.get("orderId").asText();
            UUID orderId = UUID.fromString(orderIdStr);

            log.info("Processing Payment Approved for Order: {}", orderId);
            approvedUseCase.execute(orderId);
        } catch (AmqpRejectAndDontRequeueException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error processing approved payment: {}", e.getMessage());
            // Prod: enviar para Dead Letter Queue (DLQ)
        }
    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_REJECTED_QUEUE)
    public void handlePaymentRejected(String payload) {
        try {
            log.info("Recebendo evento de Pagamento Rejeitado: {}", payload);
            JsonNode node = objectMapper.readTree(payload);

            if (!node.has("orderId")) {
                throw new AmqpRejectAndDontRequeueException("Invalid Payload: OrderId out");
            }

            String orderIdStr = node.get("orderId").asText();
            UUID orderId = UUID.fromString(orderIdStr);

            log.info("Processing Payment Rejected for Order: {}", orderId);
            rejectedUseCase.execute(orderId);
        } catch (Exception e) {
            log.error("Error processing rejected payment: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
