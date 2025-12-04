package br.com.OrderTrack.Order.domain.order;

import br.com.OrderTrack.Order.domain.exception.DomainException;
import br.com.OrderTrack.Order.domain.exception.ValidationException;
import br.com.OrderTrack.Order.domain.product.Product;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItem {

    private final UUID id;
    private final Product product;
    private final Integer quantity;
    private final BigDecimal unitPriceAtPurchase;
    private Order order;


    private OrderItem(
            Product product,
            Integer quantity
    ) {
        if (product == null || quantity <= 0) {
            throw new DomainException("All order item core must be provided.");
        }

        this.id = UUID.randomUUID();
        this.product = product;
        this.quantity = quantity;
        this.unitPriceAtPurchase = product.getPrice();
    }

    public BigDecimal calculateTotal() {
        return unitPriceAtPurchase.multiply(BigDecimal.valueOf(quantity));
    }

    public static OrderItemBuilder builder() {
        return new OrderItemBuilder();
    }

    public UUID getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    public BigDecimal getUnitPriceAtPurchase() {
        return unitPriceAtPurchase;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                ", unitPriceAtPurchase=" + unitPriceAtPurchase +
                ", order=" + order +
                '}';
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public static class OrderItemBuilder {
        private Product product;
        private Integer quantity;

        public OrderItemBuilder() {}

        public OrderItemBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public OrderItemBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderItem build() {
            if (product == null || quantity <= 0) {
                throw new ValidationException("All order item core must be provided.");
            }

            return new OrderItem(
                    this.product,
                    this.quantity
            );
        }
    }
}
