package br.com.OrderTrack.Order.infrastructure.messaging.listener;

import br.com.OrderTrack.Order.application.useCase.HandlePaymentApprovedUseCase;
import br.com.OrderTrack.Order.application.useCase.HandlePaymentRejectedUseCase;
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
public class PaymentEventListener {

    private HandlePaymentApprovedUseCase approvedUseCase;
    private HandlePaymentRejectedUseCase rejectedUseCase;

    public record PaymentEventDTO(@JsonProperty("orderId") UUID orderId) {}

    @RabbitListener(queues = RabbitConfig.PAYMENT_APPROVED_QUEUE)
    public void handlePaymentApproved(PaymentEventDTO event) {
        try {
            log.info("Recebendo evento de Pagamento Aprovado: {}", event.orderId());
            approvedUseCase.execute(event.orderId());
        } catch (Exception e) {
            log.error("Erro ao processar pagamento aprovado. Enviando para DLQ.", e);
            // Lança exceção específica para o RabbitMQ não reenfileirar infinitamente se configurado com DLQ
            throw new AmqpRejectAndDontRequeueException("Erro fatal no processamento", e);
        }
    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_REJECTED_QUEUE)
    public void handlePaymentRejected(PaymentEventDTO event) {
        try {
            log.info("Processando Pagamento Rejeitado para Pedido: {}", event.orderId());
            rejectedUseCase.execute(event.orderId());
        } catch (Exception e) {
            log.error("Erro ao processar pagamento rejeitado.", e);
            // Dependendo da regra, pode querer reenfileirar ou descartar
        }
    }
}
