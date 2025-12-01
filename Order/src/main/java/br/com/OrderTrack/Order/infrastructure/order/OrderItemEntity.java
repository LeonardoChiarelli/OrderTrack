package br.com.OrderTrack.Order.infrastructure.order;

import br.com.OrderTrack.Order.application.order.dto.OrderedItemsDTO;
import br.com.OrderTrack.Order.infrastructure.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
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

    @ManyToOne
    @JoinColumn(name = "product_id")
    private List<ProductEntity> productEntity;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    public OrderItemEntity(OrderedItemsDTO item, List<ProductEntity> productEntity) {
        this.productEntity = productEntity;
        this.quantity = item.quantity();
    }
}
