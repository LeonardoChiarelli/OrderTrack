package br.com.OrderTrack.Order.infrastructure.messaging.adapter;

import br.com.OrderTrack.Order.domain.port.out.EventPublisherPort;
import br.com.OrderTrack.Order.infrastructure.messaging.outbox.OutboxEntity;
import br.com.OrderTrack.Order.infrastructure.messaging.outbox.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RabbitEventPublisherAdapter implements EventPublisherPort {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper mapper;
    private final Tracer tracer;

    @Override
    public void publish(Object event, String eventType, String aggregateId) {
        try {
            String payload = mapper.writeValueAsString(event);

            String traceId = null;
            String spanId = null;
            if (tracer.currentSpan() != null) {
                traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
                spanId = Objects.requireNonNull(tracer.currentSpan()).context().spanId();
            }

            var outbox = OutboxEntity.builder()
                                .aggregateType("ORDER")
                                .aggregateId(aggregateId)
                                .eventType(eventType)
                                .payload(payload)
                                .createdAt(LocalDateTime.now())
                                .processed(false)
                                .traceId(traceId)
                                .spanId(spanId)
                            .build();

            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing event for Outbox");
        }
    }
}
