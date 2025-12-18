package br.com.OrderTrack.Order.infrastructure.messaging.listener;

import br.com.OrderTrack.Order.application.useCase.HandlePaymentApprovedUseCase;
import br.com.OrderTrack.Order.application.useCase.HandlePaymentRejectedUseCase;
import br.com.OrderTrack.Order.infrastructure.configuration.RabbitConfig;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.ProcessedEventEntity;
import br.com.OrderTrack.Order.infrastructure.persistence.repository.JPAProcessedEventsRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.headers.Header;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private HandlePaymentApprovedUseCase approvedUseCase;
    private HandlePaymentRejectedUseCase rejectedUseCase;
    private JPAProcessedEventsRepository processedEventsRepository;

    public record PaymentEventDTO(@JsonProperty("orderId") UUID orderId) {}

    @RabbitListener(queues = RabbitConfig.PAYMENT_APPROVED_QUEUE)
    @Transactional
    public void handlePaymentApproved(PaymentEventDTO event, String messageId) {
        try {
            processedEventsRepository.saveAndFlush(new ProcessedEventEntity(messageId, LocalDateTime.now()));
        } catch (DataIntegrityViolationException e) {
            log.warn("Evento duplicado detectado e ignorado: {}", messageId);
            return;
        }

        try {
            log.info("Recebendo evento de Pagamento Aprovado: {}", event.orderId());
            approvedUseCase.execute(event.orderId());
        } catch (Exception e) {
            log.error("Erro ao processar pagamento aprovado.", e);
            throw new AmqpRejectAndDontRequeueException("Erro fatal no processamento", e);
        }
    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_REJECTED_QUEUE)
    @Transactional
    public void handlePaymentRejected(PaymentEventDTO event, String messageId) {
        try {
            processedEventsRepository.saveAndFlush(new ProcessedEventEntity(messageId, LocalDateTime.now()));
        } catch (DataIntegrityViolationException e) {
            log.warn("Evento duplicado ignorado: {}", messageId);
            return;
        }

        try {
            log.info("Processando Pagamento Rejeitado para Pedido: {}", event.orderId());
            rejectedUseCase.execute(event.orderId());
        } catch (Exception e) {
            log.error("Erro ao processar pagamento rejeitado.", e);
        }
    }
}
