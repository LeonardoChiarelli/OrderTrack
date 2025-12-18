package br.com.OrderTrack.Order.application.dto;

import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.domain.model.OrderItem;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import br.com.OrderTrack.Order.domain.model.valueObject.Address;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDetailsDTO(
        UUID id,
        String consumerName,
        String consumerEmail,
        AddressDTO address,
        LocalDateTime orderDate,
        OrderStatus status,
        List<OrderedItemsDTO> items
) {

    public OrderDetailsDTO(Order order){
        this(
                order.getId(),
                order.getConsumerName(),
                order.getConsumerEmail(),
                new AddressDTO(
                        order.getShippingAddress().getStreet(),
                        order.getShippingAddress().getNeighborhood(),
                        order.getShippingAddress().getPostalCode(),
                        order.getShippingAddress().getCity(),
                        order.getShippingAddress().getState(),
                        order.getShippingAddress().getNumber(),
                        order.getShippingAddress().getComplement()
                ),
                order.getOrderDate(),
                order.getStatus(),
                order.getItems().stream().map(OrderedItemsDTO::new).toList()
        );
    }
}
