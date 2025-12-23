package br.com.OrderTrack.Order.domain.model;

import br.com.OrderTrack.Order.domain.exception.DomainException;
import br.com.OrderTrack.Order.domain.exception.InvalidOrderStateException;
import br.com.OrderTrack.Order.domain.model.valueObject.Address;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Order {
    private final UUID id;
    private final String consumerName;
    private final String consumerEmail;
    private final Address shippingAddress;
    private final LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private List<OrderItem> items;

    private Order(String consumerName,
                  String consumerEmail,
                  Address shippingAddress) {
        if (consumerName.isBlank() ||
                consumerEmail.isBlank() ||
                shippingAddress == null) {
            throw new DomainException("All order core must be provided.");
        }

        this.id = UUID.randomUUID();
        this.consumerName = consumerName;
        this.consumerEmail = consumerEmail;
        this.shippingAddress = shippingAddress;
        this.orderDate = LocalDateTime.now();
        this.totalPrice = BigDecimal.ZERO;
        this.status = OrderStatus.PENDING_STOCK;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public void addItem(UUID productId, Integer quantity, BigDecimal unitPrice) {
        if (this.status != OrderStatus.PENDING_STOCK) {
            throw new DomainException("Cannot add items to an order that is not in PENDING_STOCK state.");
        }

        var item = OrderItem.builder()
                .productId(productId)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .build();

        item.setOrder(this);
        this.items.add(item);
        recalculateTotal();
    }

    private void recalculateTotal() {
        this.totalPrice = items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (this.totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Total price must be greater than zero.");
        }
    }


    public void markAsPendingPayment() {
        validateStateTransition(OrderStatus.PENDING_STOCK, OrderStatus.PENDING_PAYMENT);
        this.status = OrderStatus.PENDING_PAYMENT;
    }

    public void markAsPaid() {
        if (this.status == OrderStatus.PAID) return;
        validateStateTransition(OrderStatus.PENDING_PAYMENT, OrderStatus.PAID);
        this.status = OrderStatus.PAID;
    }

    public void markAsPreparing() {
        if (status != OrderStatus.PAID) {
            throw new InvalidOrderStateException("Order status be paid.");
        }
        this.status = OrderStatus.PREPARING;
    }

    public void markAsShipped() {
        if (status != OrderStatus.PREPARING) {
            throw new InvalidOrderStateException("Order must be preparing.");
        }
        this.status = OrderStatus.SHIPPED;
    }

    public void markAsDelivered() {
        if (status != OrderStatus.SHIPPED) {
            throw new InvalidOrderStateException("Order must be shipped.");
        }
        this.status = OrderStatus.DELIVERED;
    }

    public void markAsCanceled() {
        if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED) {
            throw new InvalidOrderStateException("Cannot cancel an order that has already been shipped.");
        }
        this.status = OrderStatus.CANCELED;
    }

    private void validateStateTransition(OrderStatus expected, OrderStatus target) {
        if (this.status != expected) {
            throw new InvalidOrderStateException(String.format("Invalid transition: Cannot go from %s to %s", this.status, target));
        }
    }


    public UUID getId() {
        return id;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public String getConsumerEmail() {
        return consumerEmail;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", consumerName='" + consumerName + '\'' +
                ", consumerEmail='" + consumerEmail + '\'' +
                ", shippingAddressEntity=" + shippingAddress +
                ", orderDate=" + orderDate +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", items=" + items +
                '}';
    }

    public static class OrderBuilder {
        private String consumerName;
        private String consumerEmail;
        private Address shippingAddress;

        public OrderBuilder() {}

        public OrderBuilder consumerName(String consumerName) {
            this.consumerName = consumerName;
            return this;
        }

        public OrderBuilder consumerEmail(String consumerEmail) {
            this.consumerEmail = consumerEmail;
            return this;
        }

        public OrderBuilder shippingAddress(Address address) {
            this.shippingAddress = Address.builder()
                    .street(address.getStreet())
                    .neighborhood(address.getNeighborhood())
                    .postalCode(address.getPostalCode())
                    .city(address.getCity())
                    .state(address.getState())
                    .number(address.getNumber())
                    .complement(address.getComplement())
                .build();
            return this;
        }

        public Order build() {
            if (consumerName.isBlank() ||
                consumerEmail.isBlank() ||
                shippingAddress == null) { throw new DomainException("All Order core must be provided."); }

            return new Order(
                    this.consumerName,
                    this.consumerEmail,
                    this.shippingAddress
            );
        }
    }
}
