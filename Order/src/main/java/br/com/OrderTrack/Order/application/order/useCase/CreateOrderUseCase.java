package br.com.OrderTrack.Order.application.order.useCase;

import br.com.OrderTrack.Order.application.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.application.order.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.application.order.port.in.CreateOrderInputPort;
import br.com.OrderTrack.Order.application.port.out.InventoryGateway;
import br.com.OrderTrack.Order.application.port.out.OrderGateway;
import br.com.OrderTrack.Order.application.port.out.ProductGateway;
import br.com.OrderTrack.Order.domain.order.Order;
import br.com.OrderTrack.Order.domain.order.OrderItem;
import br.com.OrderTrack.Order.domain.order.event.PaymentRequestedEvent;
import br.com.OrderTrack.Order.domain.order.valueObject.Address;
import br.com.OrderTrack.Order.infrastructure.order.outbox.OutboxEntity;
import br.com.OrderTrack.Order.infrastructure.order.messaging.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase implements CreateOrderInputPort {

    private final OrderGateway orderGateway;
    private final InventoryGateway inventoryGateway;
    private final ProductGateway productGateway;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    public UUID execute(CreateOrderDTO dto, String userEmail, String userName) {
        List<OrderItem> domainItems = new ArrayList<>();

        for (var itemDto : dto.items()) {
            for (String productName : itemDto.productsName()) {
                var product = productGateway.findByName(productName)
                        .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productName));

                var inventory = inventoryGateway.findByProductNameWithLock(productName)
                        .orElseThrow(() -> new EntityNotFoundException("Inventory not found for: " + productName));

                inventory.decreaseQuantity(itemDto.quantity());

                inventoryGateway.save(inventory);

                domainItems.add(OrderItem.builder()
                        .product(product)
                        .quantity(itemDto.quantity())
                        .build());
            }
        }

        Address address = Address.builder()
                .street(dto.shippingAddress().street())
                .state(dto.shippingAddress().state())
                .number(dto.shippingAddress().number())
                .postalCode(dto.shippingAddress().postalCode())
                .neighborhood(dto.shippingAddress().neighborhood())
                .complement(dto.shippingAddress().complement())
                .build();

        Order order = Order.builder()
                .consumerEmail(userEmail)
                .consumerName(userName)
                .shippingAddress(address)
                .items(domainItems)
                .build();

        Order savedOrder = orderGateway.save(order);
        saveOutboxEvent(savedOrder);

        return savedOrder.getId();
    }

    private void saveOutboxEvent(Order order) {
        try {
            var event = PaymentRequestedEvent.builder()
                    .orderId(order.getId())
                    .totalPrice(order.getTotalPrice())
                    .currency("BRL")
                    .build();

            String payload = objectMapper.writeValueAsString(event);

            OutboxEntity outbox = OutboxEntity.builder()
                    .aggregateType("ORDER")
                    .aggregateId(order.getId().toString())
                    .eventType("PaymentRequested")
                    .payload(payload)
                    .createdAt(LocalDateTime.now())
                    .processed(false)
                    .build();

            outboxRepository.save(outbox);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing event", e);
        }
    }
}