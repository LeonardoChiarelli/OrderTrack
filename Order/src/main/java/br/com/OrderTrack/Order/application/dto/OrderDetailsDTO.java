package br.com.OrderTrack.Order.application.dto;

import br.com.OrderTrack.Order.infrastructure.persistence.entity.valueObject.AddressEntity;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.OrderEntity;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.OrderItemEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDetailsDTO(UUID id, String consumerName, String consumerEmail, AddressEntity shippingAddressEntity, LocalDateTime orderDate, OrderStatus status, List<OrderItemEntity> items) {

    public OrderDetailsDTO(OrderEntity orderEntity){
        this(orderEntity.getId(), orderEntity.getConsumerName(), orderEntity.getConsumerEmail(), orderEntity.getShippingAddressEntity(), orderEntity.getOrderDate(), orderEntity.getStatus(), orderEntity.getItems());
    }
}
