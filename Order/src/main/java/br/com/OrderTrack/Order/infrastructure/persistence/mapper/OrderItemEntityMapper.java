package br.com.OrderTrack.Order.infrastructure.persistence.mapper;

import br.com.OrderTrack.Order.domain.model.OrderItem;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.OrderEntity;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemEntityMapper {

    @Autowired
    private ProductEntityMapper productEntityMapper;

    public OrderItemEntity toEntity(OrderItem orderItem, OrderEntity orderEntity) {
        return new OrderItemEntity(
                orderItem,
                orderItem.getProduct().getId(), // Use o ID direto se possível ou o mapper de produto
                orderEntity
        );
    }

    public OrderItem toDomain(OrderItemEntity orderItemEntity) {
        return OrderItem.builder()
                .product(productEntityMapper.toDomain(orderItemEntity.getProductEntity()))
                .quantity(orderItemEntity.getQuantity())
                .build();
    }

}
