package br.com.OrderTrack.Order.application.order.dto;

import br.com.OrderTrack.Order.infrastructure.order.valueObject.Address;
import br.com.OrderTrack.Order.infrastructure.order.OrderEntity;
import br.com.OrderTrack.Order.infrastructure.order.OrderItemEntity;
import br.com.OrderTrack.Order.infrastructure.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailsDTO(Long id, String consumerName, String consumerEmail, Address shippingAddress, LocalDateTime orderDate, OrderStatus status, List<OrderItemEntity> items) {

    public OrderDetailsDTO(OrderEntity orderEntity){
        this(orderEntity.getId(), orderEntity.getConsumerName(), orderEntity.getConsumerEmail(), orderEntity.getShippingAddress(), orderEntity.getOrderDate(), orderEntity.getStatus(), orderEntity.getItems());
    }
}
