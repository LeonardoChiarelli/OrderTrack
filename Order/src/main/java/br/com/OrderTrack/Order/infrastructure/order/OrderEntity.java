package br.com.OrderTrack.Order.infrastructure.order;

import br.com.OrderTrack.Order.application.order.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.infrastructure.order.valueObject.Address;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String consumerName;
    private String consumerEmail;

    @Embedded
    private Address shippingAddress;

    private LocalDateTime orderDate;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    public OrderEntity(@Valid CreateOrderDTO dto, Address address, BigDecimal totalPrice, List<OrderItemEntity> items) {
        this.consumerName = dto.consumerName();
        this.consumerEmail = dto.consumerEmail();
        this.shippingAddress = address;
        this.orderDate = LocalDateTime.now();
        this.totalPrice = totalPrice;
        this.status = OrderStatus.NEW;
        this.items = items;
    }

    public void changeStatus(@NotNull OrderStatus status) {
        this.status = status;
    }
}
