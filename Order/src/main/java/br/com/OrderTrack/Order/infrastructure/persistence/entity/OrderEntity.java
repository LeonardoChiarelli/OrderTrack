package br.com.OrderTrack.Order.infrastructure.persistence.entity;

import br.com.OrderTrack.Order.application.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.domain.exception.InvalidOrderStateException;
import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.valueObject.AddressEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class OrderEntity {

    @Id
    private UUID id;

    private String consumerName;
    private String consumerEmail;

    @Embedded
    private AddressEntity shippingAddressEntity;

    private LocalDateTime orderDate;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItemEntity> items = new ArrayList<>();

    public OrderEntity(Order order, AddressEntity addressEntity) {
        this.id = order.getId();
        this.consumerName = order.getConsumerName();
        this.consumerEmail = order.getConsumerEmail();
        this.shippingAddressEntity = addressEntity;
        this.orderDate = order.getOrderDate();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
    }
}
