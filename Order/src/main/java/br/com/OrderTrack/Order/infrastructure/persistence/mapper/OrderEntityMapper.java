package br.com.OrderTrack.Order.infrastructure.persistence.mapper;

import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.OrderEntity;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.valueObject.AddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEntityMapper {

    @Autowired
    private AddressEntityMapper addressEntityMapper;

    @Autowired
    private OrderItemEntityMapper orderItemEntityMapper;

    public OrderEntity toEntity(Order order) {
        var orderEntity = new OrderEntity(order, new AddressEntity(order.getShippingAddress()));

        var itemsEntities = order.getItems().stream()
                .map(item -> orderItemEntityMapper.toEntity(item, orderEntity))
                .toList();

        orderEntity.getItems().addAll(itemsEntities);
        return orderEntity;
    }

    public Order toDomain(OrderEntity orderEntity) {
        return Order.builder()
                .consumerName(orderEntity.getConsumerName())
                .consumerEmail(orderEntity.getConsumerEmail())
                .shippingAddress(addressEntityMapper.toDomain(orderEntity.getShippingAddressEntity()))
                .items(orderEntity.getItems().stream()
                        .map(orderItemEntityMapper::toDomain)
                        .toList())
                .totalPrice(orderEntity.getTotalPrice())
                .build();
    }
}
