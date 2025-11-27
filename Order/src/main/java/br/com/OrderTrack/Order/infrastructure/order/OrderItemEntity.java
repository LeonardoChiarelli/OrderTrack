package br.com.OrderTrack.Order.infrastructure.order;

import br.com.OrderTrack.Order.application.order.dto.OrderedItemsDTO;
import br.com.OrderTrack.Order.infrastructure.product.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class OrderItemEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    private Integer quantity;
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    public OrderItemEntity(OrderedItemsDTO item, ProductEntity productEntity) {
        this.productEntity = productEntity;
        this.quantity = item.quantity();
        this.unitPrice = item.unitPrice();
    }
}
