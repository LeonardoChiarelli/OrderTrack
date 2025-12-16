package br.com.OrderTrack.Order.domain.model;

import br.com.OrderTrack.Order.domain.exception.DomainException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class OrderItem {

    private final UUID id;
    private final UUID productId;
    private final BigDecimal unitPrice;
    private final Integer quantity;
    private Order order;


    private OrderItem(
            UUID productId,
            Integer quantity,
            BigDecimal unitPrice
    ) {
        if (productId == null || quantity <= 0 || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("All order item core must be provided.");
        }

        this.id = UUID.randomUUID();
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public static OrderItemBuilder builder() {
        return new OrderItemBuilder();
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", productId=" + productId +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", order=" + order +
                '}';
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(getId(), orderItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public static class OrderItemBuilder {
        private UUID productId;
        private Integer quantity;
        private BigDecimal unitPrice;

        public OrderItemBuilder() {}

        public OrderItemBuilder productId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public OrderItemBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderItemBuilder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public OrderItem build() {
            if (productId == null || quantity <= 0 || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
                throw new DomainException("All order item core must be provided.");
            }

            return new OrderItem(
                    this.productId,
                    this.quantity,
                    this.unitPrice
            );
        }
    }
}
