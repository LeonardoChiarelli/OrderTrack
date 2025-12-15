package br.com.OrderTrack.Order.application.order.useCase;

import br.com.OrderTrack.Order.application.order.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.application.order.port.in.CreateOrderInputPort;
import br.com.OrderTrack.Order.application.order.port.out.OrderRepositoryPort;
import br.com.OrderTrack.Order.domain.order.Order;
import br.com.OrderTrack.Order.domain.order.OrderItem;
import br.com.OrderTrack.Order.domain.order.event.PaymentRequestedEvent;
import br.com.OrderTrack.Order.infrastructure.inventory.JpaInventoryRepository;
import br.com.OrderTrack.Order.infrastructure.order.outbox.OutboxEntity;
import br.com.OrderTrack.Order.infrastructure.product.JpaProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CreateOrderUseCase implements CreateOrderInputPort {

    private final OrderRepositoryPort orderRepository;
    private final JpaInventoryRepository inventoryRepository;
    private final JpaProductRepository productRepository;
    private final OutboxRepository outboxRepository; // trocar para porta
    private final ObjectMapper objectMapper;

    public CreateOrderUseCase(OrderRepositoryPort orderRepository,
                              JpaInventoryRepository inventoryRepository,
                              JpaProductRepository productRepository,
                              OutboxRepository outboxRepository,
                              ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    @Transactional
    public UUID execute(CreateOrderDTO dto) {
        List<OrderItem> items = new ArrayList<>();

        dto.items().forEach(itemDto -> {
            // ... lógica de busca de produto e validação ...
            // inventoryRepository.findByProductNameWithLock(...)
        });

        // 2. Criar Entidade de Domínio
        Order order = Order.builder()
                .consumerName(dto.consumerName())
                .consumerEmail(dto.consumerEmail())
                // ... resto do builder ...
                .build();

        // 3. Salvar Pedido
        Order savedOrder = orderRepository.save(order);

        // 4. Criar Evento de Domínio
        PaymentRequestedEvent event = PaymentRequestedEvent.builder()
                .orderId(savedOrder.getId())
                .totalPrice(savedOrder.getTotalPrice())
                .currency("BRL")
                .build();

        // 5. Salvar na Outbox (Ao invés de mandar direto pro Rabbit)
        try {
            String eventPayload = objectMapper.writeValueAsString(event);
            OutboxEntity outbox = OutboxEntity.builder()
                    .aggregateType("ORDER")
                    .aggregateId(savedOrder.getId().toString())
                    .eventType("PaymentRequested")
                    .payload(eventPayload)
                    .createdAt(LocalDateTime.now())
                    .processed(false)
                    .build();

            outboxRepository.save(outbox);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao serializar evento", e);
        }

        return savedOrder.getId();
    }
}
