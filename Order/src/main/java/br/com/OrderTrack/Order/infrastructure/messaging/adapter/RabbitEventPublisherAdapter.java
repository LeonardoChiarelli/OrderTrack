package br.com.OrderTrack.Order.infrastructure.messaging.adapter;

import br.com.OrderTrack.Order.domain.port.out.EventPublisherPort;
import br.com.OrderTrack.Order.infrastructure.messaging.outbox.OutboxEntity;
import br.com.OrderTrack.Order.infrastructure.messaging.outbox.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RabbitEventPublisherAdapter implements EventPublisherPort {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper mapper;

    public void publish(Object event, String eventType, String aggregateId) {
        try {
            String payload = mapper.writeValueAsString(event);

            var outbox = OutboxEntity.builder()
                                .aggregateType("ORDER")
                                .aggregateId(aggregateId)
                                .eventType(eventType)
                                .payload(payload)
                                .createdAt(LocalDateTime.now())
                                .processed(false)
                            .build();

            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing event for Outbox");
        }
    }
}
