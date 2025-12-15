package br.com.OrderTrack.Order.infrastructure.order.persistence.mapper;

import br.com.OrderTrack.Order.domain.order.Order;
import br.com.OrderTrack.Order.infrastructure.order.persistence.entity.OrderEntity;
import br.com.OrderTrack.Order.infrastructure.order.persistence.entity.valueObject.AddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEntityMapper {

    @Autowired
    private AddressEntityMapper addressEntityMapper;

    @Autowired
    private OrderItemEntityMapper orderItemEntityMapper;

    public OrderEntity toEntity(Order order) {
        return new OrderEntity(order, new AddressEntity(order.getShippingAddress()));
    }

    public Order toDomain(OrderEntity orderEntity) {
        return Order.builder()
                .consumerName(orderEntity.getConsumerName())
                .consumerEmail(orderEntity.getConsumerEmail())
                .shippingAddress(addressEntityMapper.
                        toDomain(orderEntity.getShippingAddressEntity()))
                .items(orderEntity.getItems().stream()
                        .map(i -> orderItemEntityMapper.toDomain(i))
                        .toList())
            .build();
    }
}
