package br.com.OrderTrack.Order.application.useCase;

import br.com.OrderTrack.Order.application.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.domain.event.OrderCreatedEvent;
import br.com.OrderTrack.Order.domain.exception.DomainException;
import br.com.OrderTrack.Order.domain.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.domain.model.OrderItem;
import br.com.OrderTrack.Order.domain.model.valueObject.Address;
import br.com.OrderTrack.Order.domain.model.valueObject.Product;
import br.com.OrderTrack.Order.domain.port.in.CreateOrderInputPort;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import br.com.OrderTrack.Order.domain.port.out.ProductGateway;
import br.com.OrderTrack.Order.infrastructure.messaging.adapter.RabbitEventPublisherAdapter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        List<OrderItem> items = new ArrayList<>();
        BigDecimal totalPriceOrder = BigDecimal.ZERO;

        for (var itemDto : dto.items()) {
            var product = productGateway.findById(itemDto.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + itemDto.productId()));

            if (!product.isActive()) {
                throw new DomainException("Produto inativo: " + itemDto.productId());
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDto.quantity()));
            totalPriceOrder = totalPriceOrder.add(itemTotal);

            items.add(OrderItem.builder()
                    .productId(product.getId())
                    .quantity(itemDto.quantity())
                    .unitPrice(product.getPrice())
                .build());
        }
        var order = Order.builder()
                .consumerName(userName)
                .consumerEmail(userEmail)
                .shippingAddress(address)
                .items(items)
                .totalPrice(totalPriceOrder)
                .build();

        var savedOrder = orderGateway.save(order);

        List<OrderCreatedEvent.OrderItemEventDTO> eventItems = savedOrder.getItems().stream()
                .map(i -> new OrderCreatedEvent.OrderItemEventDTO(i.getProductId(), i.getQuantity()))
                .toList();

        eventPublisher.publish(new OrderCreatedEvent(savedOrder.getId(), eventItems), "OrderCreatedEvent", savedOrder.getId().toString());

        return savedOrder.getId();
    }
}