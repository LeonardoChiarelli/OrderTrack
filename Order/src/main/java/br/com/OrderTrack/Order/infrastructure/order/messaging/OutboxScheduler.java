package br.com.OrderTrack.Order.infrastructure.order.messaging;

import br.com.OrderTrack.Order.infrastructure.order.outbox.OutboxEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxScheduler {

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate; // Do Spring AMQP

    @Scheduled(fixedRate = 2000) // Roda a cada 2 segundos
    @Transactional
    public void processOutbox() {
        List<OutboxEntity> unprocessed = outboxRepository.findByProcessedFalse();

        for (OutboxEntity event : unprocessed) {
            rabbitTemplate.convertAndSend("order-exchange", "", event.getPayload());

            event.setProcessed(true);
            outboxRepository.save(event);
        }
    }
}
