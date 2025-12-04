package br.com.OrderTrack.Order.infrastructure.order;

import br.com.OrderTrack.Order.application.order.dto.OrderedItemsDTO;
import br.com.OrderTrack.Order.infrastructure.product.ProductEntity;
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
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    private Integer quantity;
    private BigDecimal unitPriceAtPurchase;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    public OrderItemEntity(OrderedItemsDTO item, ProductEntity productEntity) {
        this.productEntity = productEntity;
        this.quantity = item.quantity();
        this.unitPriceAtPurchase = productEntity.getPrice();
    }

    public BigDecimal calculateTotal() {
        return unitPriceAtPurchase.multiply(BigDecimal.valueOf(quantity));
    }

    public void setOrder(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }
}
