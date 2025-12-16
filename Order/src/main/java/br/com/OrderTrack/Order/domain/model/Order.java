package br.com.OrderTrack.Order.domain.model;

import br.com.OrderTrack.Order.domain.exception.DomainException;
import br.com.OrderTrack.Order.domain.exception.InvalidOrderStateException;
import br.com.OrderTrack.Order.domain.model.valueObject.Address;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final String consumerName;
    private final String consumerEmail;
    private final Address shippingAddress;
    private final LocalDateTime orderDate;
    private final BigDecimal totalPrice;
    private OrderStatus status;
    private final List<OrderItem> items;

    private Order(String consumerName,
                  String consumerEmail,
                  Address shippingAddress,
                  List<OrderItem> items,
                  BigDecimal totalPrice) {
        if (consumerName.isBlank() ||
                consumerEmail.isBlank() ||
                shippingAddress == null ||
                items.isEmpty() ||
                totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("All order core must be provided.");
        }

        this.id = UUID.randomUUID();
        this.consumerName = consumerName;
        this.consumerEmail = consumerEmail;
        this.shippingAddress = shippingAddress;
        this.orderDate = LocalDateTime.now();
        this.totalPrice = totalPrice;
        this.status = OrderStatus.PENDING_STOCK;
        this.items = items;
        items.forEach(i -> i.setOrder(this));
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }


    public void markAsPendingPayment() {
        if (status != OrderStatus.PENDING_STOCK) {
            throw new InvalidOrderStateException("Order must be pending payment.");
        }
        this.status = OrderStatus.PENDING_PAYMENT;
    }

    public void markAsPaid() {
        if (status != OrderStatus.PENDING_PAYMENT) {
            throw new InvalidOrderStateException("Order must be new.");
        }
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
        if (status != OrderStatus.PENDING_STOCK && status != OrderStatus.PENDING_PAYMENT) {
            throw new InvalidOrderStateException("Order cannot be canceled.");
        }
        this.status = OrderStatus.CANCELED;
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
        return items;
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
        private List<OrderItem> items = new ArrayList<>();
        private BigDecimal totalPrice;

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

        public OrderBuilder items(List<OrderItem> items) {
            this.items.addAll(items);
            return this;
        }

        public OrderBuilder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Order build() {
            if (consumerName.isBlank() ||
                consumerEmail.isBlank() ||
                shippingAddress == null ||
                items.isEmpty() ||
                totalPrice.compareTo(BigDecimal.ZERO) <= 0
            ) { throw new DomainException("All Order core must be provided."); }

            return new Order(
                    this.consumerName,
                    this.consumerEmail,
                    this.shippingAddress,
                    this.items,
                    this.totalPrice = totalPrice
            );
        }
    }
}
