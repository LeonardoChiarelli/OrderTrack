package br.com.OrderTrack.Order.infrastructure.order.persistence.entity;

import br.com.OrderTrack.Order.application.order.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.domain.order.Order;
import br.com.OrderTrack.Order.domain.order.OrderStatus;
import br.com.OrderTrack.Order.infrastructure.order.persistence.entity.valueObject.AddressEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    public void changeStatus(@NotNull OrderStatus status) {
        this.status = status;
    }
}
