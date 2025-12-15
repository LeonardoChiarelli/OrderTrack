package br.com.OrderTrack.Order.domain.order;

import br.com.OrderTrack.Order.domain.exception.DomainException;
import br.com.OrderTrack.Order.domain.exception.InvalidOrderStateException;
import br.com.OrderTrack.Order.domain.exception.ValidationException;
import br.com.OrderTrack.Order.domain.order.event.OrderCreatedEvent;
import br.com.OrderTrack.Order.domain.order.valueObject.Address;

import java.math.BigDecimal;
import java.time.LocalDate;
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
                  List<OrderItem> items) {
        if (consumerName.isBlank() ||
                consumerEmail.isBlank() ||
                shippingAddress == null ||
                items.isEmpty()) {
            throw new DomainException("All order core must be provided.");
        }

        this.id = UUID.randomUUID();
        this.consumerName = consumerName;
        this.consumerEmail = consumerEmail;
        this.shippingAddress = shippingAddress;
        this.orderDate = LocalDateTime.now();
        this.totalPrice = items.stream()
                .map(OrderItem::calculateTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.status = OrderStatus.NEW;
        this.items = items;
        items.forEach(i -> i.setOrder(this));
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public void markAsProcessing() {
        if (status != OrderStatus.NEW) {
            throw new InvalidOrderStateException("Order must be new.");
        }
        this.status = OrderStatus.PROCESSING;
    }

    public void markAsPackage() {
        if (status != OrderStatus.PROCESSING) {
            throw new InvalidOrderStateException("Order status be processing.");
        }
        this.status = OrderStatus.PACKAGE;
    }

    public void markAsOutForDelivery() {
        if (status != OrderStatus.PACKAGE) {
            throw new InvalidOrderStateException("Order must be package.");
        }
        this.status = OrderStatus.OUT_FOR_DELIVERY;
    }

    public void markAsDelivered() {
        if (status != OrderStatus.OUT_FOR_DELIVERY) {
            throw new InvalidOrderStateException("Order must be out for delivery.");
        }
        this.status = OrderStatus.DELIVERED;
    }

    public void markAsCanceled() {
        if (status != OrderStatus.PROCESSING) {
            throw new InvalidOrderStateException("Order must be processing.");
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

        public Order build() {
            if (consumerName.isBlank() ||
                consumerEmail.isBlank() ||
                shippingAddress == null ||
                items.isEmpty()
            ) { throw new ValidationException("All Order core must be provided."); }

            return new Order(
                    this.consumerName,
                    this.consumerEmail,
                    this.shippingAddress,
                    this.items
            );
        }
    }
}
