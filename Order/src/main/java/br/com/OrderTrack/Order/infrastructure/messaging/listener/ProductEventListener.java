package br.com.OrderTrack.Order.infrastructure.messaging.listener;

import br.com.OrderTrack.Order.infrastructure.configuration.RabbitConfig;
import br.com.OrderTrack.Order.infrastructure.persistence.adapter.ProductReplicaAdapter;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.ProductReplicaEntity;
import br.com.OrderTrack.Order.infrastructure.persistence.repository.JPAProductReplicaRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventListener {

    private final JPAProductReplicaRepository repository;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ProductEventDTO(UUID id, String name, BigDecimal price, boolean active) {}

    @RabbitListener(queues = RabbitConfig.PRODUCT_EVENTS_QUEUE)
    @Transactional
    public void handleProductEvent(ProductEventDTO event) {
        log.info("Sincronizando produto: {} - {}", event.id, event.name);

        var entity = new ProductReplicaEntity(
                event.id,
                event.name,
                event.price,
                event.active
        );

        repository.save(entity);
    }
}
