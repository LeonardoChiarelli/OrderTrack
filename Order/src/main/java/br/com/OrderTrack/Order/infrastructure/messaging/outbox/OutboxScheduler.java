package br.com.OrderTrack.Order.infrastructure.messaging.outbox;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
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
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedRate = 2000)
    @SchedulerLock(name = "OutboxScheduler_processOutbox", lockAtLeastFor = "PT1S", lockAtMostFor = "PT10S")
    @Transactional
    public void processOutbox() {
        List<OutboxEntity> unprocessed = outboxRepository.findByProcessedFalse();

        for (OutboxEntity event : unprocessed) {
            rabbitTemplate.convertAndSend("order-exchange", "", event.getPayload(), m -> {
                m.getMessageProperties().setHeader("X-Correlation-ID", event.getId().toString());
                return m;
            });

            event.setProcessed(true);
            outboxRepository.save(event);
        }
    }
}