package br.com.OrderTrack.Order.infrastructure.order.messaging.listener;

import br.com.OrderTrack.Order.application.useCase.HandlePaymentApprovedUseCase;
import br.com.OrderTrack.Order.application.useCase.HandlePaymentRejectedUseCase;
import br.com.OrderTrack.Order.infrastructure.order.messaging.config.RabbitConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
            JsonNode node = objectMapper.readTree(payload);
            String orderIdStr = node.get("orderId").asText();
            UUID orderId = UUID.fromString(orderIdStr);

            System.out.println("Processing Payment Approved for Order: " + orderId);
            approvedUseCase.execute(orderId);
        } catch (Exception e) {
            System.err.println("Error processing approved payment: " + e.getMessage());
            // Prod: enviar para Dead Letter Queue (DLQ)
        }
    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_REJECTED_QUEUE)
    public void handlePaymentRejected(String payload) {
        try {
            JsonNode node = objectMapper.readTree(payload);
            String orderIdStr = node.get("orderId").asText();
            UUID orderId = UUID.fromString(orderIdStr);

            System.out.println("Processing Payment Rejected for Order: " + orderId);
            rejectedUseCase.execute(orderId);
        } catch (Exception e) {
            System.err.println("Error processing rejected payment: " + e.getMessage());
        }
    }
}
