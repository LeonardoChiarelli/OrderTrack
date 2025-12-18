package br.com.OrderTrack.Order.infrastructure.messaging.outbox;

import io.micrometer.tracing.Tracer;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OutboxScheduler {

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Tracer tracer;

    @Scheduled(fixedRate = 500)
    @SchedulerLock(name = "OutboxScheduler_processOutbox", lockAtLeastFor = "PT0.1S", lockAtMostFor = "PT2S")
    @Transactional
    public void processOutbox() {
        var pageRequest = PageRequest.of(0, 100, Sort.by("createdAt").ascending());
        List<OutboxEntity> unprocessed = outboxRepository.findByProcessedFalse(pageRequest);

        for (OutboxEntity event : unprocessed) {
            rabbitTemplate.convertAndSend("order-exchange", "", event.getPayload(), m -> {
                MessageProperties properties = m.getMessageProperties();
                properties.setHeader("X-Correlation-ID", event.getId().toString());

                if (event.getTraceId() != null && event.getSpanId() != null) {
                    String traceParent = String.format("00-%s-%s-01", event.getTraceId(), event.getSpanId());
                    properties.setHeader("traceparent", traceParent);

                    properties.setHeader("X-B3-TraceId", event.getTraceId());
                    properties.setHeader("X-B3-SpanId", event.getSpanId());
                }
                return m;
            });

            event.setProcessed(true);
            outboxRepository.save(event);
        }
    }

    @Scheduled(cron = "0 0 3 * * *")
    @SchedulerLock(name = "OutboxScheduler_purge", lockAtLeastFor = "PT1M", lockAtMostFor = "PT5M")
    @Transactional
    public void purgeOldEvents() {
        outboxRepository.deleteProcessedOlderThan(LocalDateTime.now().minusDays(7));
    }
}