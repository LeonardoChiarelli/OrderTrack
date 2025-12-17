package br.com.OrderTrack.Order.infrastructure.persistence.entity;

import br.com.OrderTrack.Order.domain.model.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode(of = "id")
@ToString
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    private BigDecimal unitPrice;

    private Integer quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    public OrderItemEntity(OrderItem orderItem, UUID productId, OrderEntity orderEntity) {
        this.id = orderItem.getId();
        this.productId = productId;
        this.quantity = orderItem.getQuantity();
        this.orderEntity = orderEntity;
        this.unitPrice = orderItem.getUnitPrice();
    }

    public void setOrder(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }
}
