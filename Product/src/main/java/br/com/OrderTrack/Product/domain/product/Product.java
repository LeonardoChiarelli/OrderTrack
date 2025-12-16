package br.com.OrderTrack.Product.domain.product;

import br.com.OrderTrack.Order.domain.exception.DomainException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Product {
    private final UUID id;
    private final String name;
    private final String description;
    private final String category;
    private final BigDecimal price;
    private boolean active;

    private Product(
            String name,
            String description,
            String category,
            BigDecimal price
    ) {
        if (name.isBlank() || description.isBlank() || category.isBlank() || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("All product core must be provided. ");
        }

        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.active = true;
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public void changeStatus(boolean status) {
        this.active = status;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
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
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", active=" + active +
                '}';
    }

    public static class ProductBuilder {
        private String name;
        private String description;
        private String category;
        private BigDecimal price;

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder category(String category) {
            this.category = category;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Product build() {
            if (this.name.isBlank() || this.description.isBlank() || this.category.isBlank() || this.price.compareTo(BigDecimal.ZERO) <= 0) {
             throw new ValidationException("All product core must be provided.");
            }

            return new Product(name, description, category, price);
        }
    }
}
