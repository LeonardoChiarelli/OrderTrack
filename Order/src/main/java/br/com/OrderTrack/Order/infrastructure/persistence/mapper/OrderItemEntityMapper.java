package br.com.OrderTrack.Order.infrastructure.persistence.mapper;

import br.com.OrderTrack.Order.domain.model.OrderItem;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.OrderItemEntity;
import br.com.OrderTrack.Order.infrastructure.product.gateways.ProductEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemEntityMapper {

    @Autowired
    private ProductEntityMapper productEntityMapper;

    @Autowired
    private OrderEntityMapper orderEntityMapper;

    public OrderItemEntity toEntity(OrderItem orderItem) {
        return new OrderItemEntity(orderItem, productEntityMapper.toEntity(orderItem.getProduct()), orderEntityMapper.toEntity(orderItem.getOrder()));
    }

    public OrderItem toDomain(OrderItemEntity orderItemEntity) {
        return OrderItem.builder()
                .product(productEntityMapper.toDomain(orderItemEntity.getProductEntity()))
                .quantity(orderItemEntity.getQuantity())
            .build();
    }

}
