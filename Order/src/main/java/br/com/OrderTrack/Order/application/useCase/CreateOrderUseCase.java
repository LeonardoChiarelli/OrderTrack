package br.com.OrderTrack.Order.application.useCase;

import br.com.OrderTrack.Order.application.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.domain.event.OrderCreatedEvent;
import br.com.OrderTrack.Order.domain.exception.DomainException;
import br.com.OrderTrack.Order.domain.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.domain.model.OrderItem;
import br.com.OrderTrack.Order.domain.model.valueObject.Address;
import br.com.OrderTrack.Order.domain.port.in.CreateOrderInputPort;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import br.com.OrderTrack.Order.domain.port.out.ProductGateway;
import br.com.OrderTrack.Order.infrastructure.messaging.adapter.RabbitEventPublisherAdapter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase implements CreateOrderInputPort {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;
    private final RabbitEventPublisherAdapter eventPublisher;
    private final Counter orderCreatedCounter;

    public CreateOrderUseCase(OrderGateway orderGateway,
                              ProductGateway productGateway,
                              RabbitEventPublisherAdapter eventPublisher,
                              MeterRegistry registry) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
        this.eventPublisher = eventPublisher;

        this.orderCreatedCounter = Counter.builder("order_created_total")
                .description("Total de pedidos criados com sucesso")
                .register(registry);
    }

    @Override
    @Transactional
    public UUID execute(CreateOrderDTO dto, String userEmail, String userName) {
        var address = Address.builder()
                .street(dto.shippingAddress().street())
                .number(dto.shippingAddress().number())
                .neighborhood(dto.shippingAddress().neighborhood())
                .city(dto.shippingAddress().city())
                .state(dto.shippingAddress().state())
                .postalCode(dto.shippingAddress().postalCode())
                .complement(dto.shippingAddress().complement())
                .build();

        var order = Order.builder()
                .consumerName(userName)
                .consumerEmail(userEmail)
                .shippingAddress(address)
                .build();

        for (var itemDto : dto.items()) {
            var product = productGateway.findById(itemDto.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + itemDto.productId()));

            if (!product.isActive()) {
                throw new DomainException("Product inactive: " + itemDto.productId());
            }

            order.addItem(product.getId(), itemDto.quantity(), product.getPrice());
        }

        var savedOrder = orderGateway.save(order);

        List<OrderCreatedEvent.OrderItemEventDTO> eventItems = savedOrder.getItems().stream()
                .map(i -> new OrderCreatedEvent.OrderItemEventDTO(i.getProductId(), i.getQuantity()))
                .toList();

        eventPublisher.publish(new OrderCreatedEvent(savedOrder.getId(), eventItems), "OrderCreatedEvent", savedOrder.getId().toString());

        orderCreatedCounter.increment();

        return savedOrder.getId();
    }
}