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
    @GeneratedValue(strategy = GenerationType.UUID)
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

    public OrderEntity(@Valid CreateOrderDTO dto, AddressEntity addressEntity, List<OrderItemEntity> items) {
        this.consumerName = dto.consumerName();
        this.consumerEmail = dto.consumerEmail();
        this.shippingAddressEntity = addressEntity;
        this.orderDate = LocalDateTime.now();
        this.totalPrice = items.stream()
                .map(OrderItemEntity::calculateTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.status = OrderStatus.NEW;
        this.items = items;
        items.forEach(i -> i.setOrder(this));
    }

    public OrderEntity(Order order, AddressEntity addressEntity) {
        this.id = order.getId();
        this.consumerName = order.getConsumerName();
        this.consumerEmail = order.getConsumerEmail();
        this.shippingAddressEntity = addressEntity;
        this.orderDate = order.getOrderDate();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
    }

    public void markAsProcessing() {
        if (status != OrderStatus.NEW) {
            throw new InvalidOrderStateException("Order must be new.");
        }
        this.status = OrderStatus.PROCESSING;
    }

    public void markAsPackage() {
        if (status != OrderStatus.PROCESSING) {
            throw new InvalidOrderStateException("Order status be processing.");
        }
        this.status = OrderStatus.PACKAGE;
    }

    public void markAsOutForDelivery() {
        if (status != OrderStatus.PACKAGE) {
            throw new InvalidOrderStateException("Order must be package.");
        }
        this.status = OrderStatus.OUT_FOR_DELIVERY;
    }

    public void markAsDelivered() {
        if (status != OrderStatus.OUT_FOR_DELIVERY) {
            throw new InvalidOrderStateException("Order must be out for delivery.");
        }
        this.status = OrderStatus.DELIVERED;
    }

    public void markAsCanceled() {
        if (status != OrderStatus.PROCESSING) {
            throw new InvalidOrderStateException("Order must be processing.");
        }
        this.status = OrderStatus.CANCELED;
    }
}
