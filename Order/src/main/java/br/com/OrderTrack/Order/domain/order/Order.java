package br.com.OrderTrack.Order.domain.order;

import br.com.OrderTrack.Order.application.exception.DomainException;
import br.com.OrderTrack.Order.application.exception.ValidationException;
import br.com.OrderTrack.Order.domain.order.valueObject.Address;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final String consumerName;
    private final String consumerEmail;
    private final Address shippingAddress;
    private final LocalDateTime orderDate;
    private final BigDecimal totalPrice;
    private OrderStatus status;
    private final List<OrderItem> items = new ArrayList<>();

    private Order(UUID id,
                  String consumerName,
                  String consumerEmail,
                  Address shippingAddress,
                  LocalDateTime orderDate,
                  BigDecimal totalPrice,
                  OrderStatus status,
                  List<OrderItem> items) {
        if (id == null ||
                consumerName.isBlank() ||
                consumerEmail.isBlank() ||
                shippingAddress == null ||
                orderDate == null ||
                totalPrice.compareTo(BigDecimal.ZERO) <= 0 ||
                status == null ||
                items.isEmpty()) {
            throw new DomainException("All order core must be provided.");
        }

        this.id = id;
        this.consumerName = consumerName;
        this.consumerEmail = consumerEmail;
        this.shippingAddress = shippingAddress;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.items.addAll(items);
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public void changeStatus(OrderStatus status) { this.status = status; }

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

        public BigDecimal calculateTotalPrice() {
            return  (BigDecimal) items.stream()
                    .map(item -> {
                        return item.getProduct().getPrice().muliply(BigDecimal.valueOf(item.getQuantity()));
                    });
        }

        public OrderBuilder consumerName(String consumerName) {
            this.consumerName = consumerName;
            return this;
        }

        public OrderBuilder consumerEmail(String consumerEmail) {
            this.consumerEmail = consumerEmail;
            return this;
        }

        public OrderBuilder shippingAddress(String street,
                                            String neighborhood,
                                            String postalCode,
                                            String city,
                                            String state,
                                            String number,
                                            String complement) {
            this.shippingAddress = null;// Builder;
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

            return new Order(UUID.randomUUID(),
                    this.consumerName,
                    this.consumerEmail,
                    this.shippingAddress,
                    LocalDateTime.now(),
                    calculateTotalPrice(),
                    OrderStatus.NEW,
                    items);
        }
    }
}
