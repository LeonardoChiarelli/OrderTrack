package br.com.OrderTrack.Order.domain.model.valueObject;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Product {
    private final UUID id;
    private final String name;
    private final BigDecimal price;
    private final boolean active;

    private Product(UUID id, String name, BigDecimal price, boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.active = active;
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", active=" + active +
                '}';
    }

    public static class ProductBuilder {
        private UUID id;
        private String name;
        private BigDecimal price;
        private boolean active;

        public ProductBuilder() {}


        public ProductBuilder id(UUID id){
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name){
            this.name = name;
            return this;
        }

        public ProductBuilder price(BigDecimal price){
            this.price = price;
            return this;
        }

        public ProductBuilder active(boolean active){
            this.active = active;
            return this;
        }

        public Product build() {
            return new Product(
                    this.id,
                    this.name,
                    this.price,
                    this.active
            );
        }
    }
}
