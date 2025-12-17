package br.com.OrderTrack.Order.infrastructure.persistence.mapper;

import br.com.OrderTrack.Order.domain.model.OrderItem;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.OrderEntity;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemEntityMapper {

    public OrderItemEntity toEntity(OrderItem orderItem, OrderEntity orderEntity) {
        return new OrderItemEntity(
                orderItem,
                orderItem.getProductId(),
                orderEntity
        );
    }

    public OrderItem toDomain(OrderItemEntity orderItemEntity) {
        return OrderItem.builder()
                .productId(orderItemEntity.getProductId())
                .quantity(orderItemEntity.getQuantity())
                .unitPrice(orderItemEntity.getOrderEntity().getTotalPrice())
                .build();
    }

}
