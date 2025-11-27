package br.com.OrderTrack.Track.domain.model;

import br.com.OrderTrack.Track.domain.dto.CreateOrderDTO;
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
public class Order {

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
    private List<OrderItem> items = new ArrayList<>();

    public Order(@Valid CreateOrderDTO dto, Address address, BigDecimal totalPrice, List<OrderItem> items) {
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
