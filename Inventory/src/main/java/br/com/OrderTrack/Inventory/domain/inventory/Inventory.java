package br.com.OrderTrack.Inventory.domain.inventory;

import br.com.OrderTrack.Order.domain.exception.BusinessRule;
import br.com.OrderTrack.Order.domain.exception.DomainException;
import br.com.OrderTrack.Order.domain.exception.ValidationException;
import br.com.OrderTrack.Order.domain.product.Product;

import java.util.Objects;
import java.util.UUID;

public class Inventory {
    private final UUID id;
    private Integer quantity;
    private final Product product;
    private Integer version;

    private Inventory(Integer quantity, Product product) {
        if (quantity <= 0 || product == null) {
            throw new DomainException("All Inventory core must be provided");
        }

        this.id = UUID.randomUUID();
        this.quantity = quantity;
        this.product = product;
        this.version = 1;
    }

    public static InventoryBuilder builder() {
        return new InventoryBuilder();
    }

    public void addQuantity(Integer quantity) {
        if (quantity <= 0) { throw new ValidationException("Quantity must be greater then 0."); }

        this.quantity += quantity;
        this.version += 1;
    }

    public void decreaseQuantity(Integer quantity) {
        if (quantity <= 0) { throw new ValidationException("Quantity must be greater than 0."); }
        if (getQuantity() < quantity) { throw new BusinessRule("Quantity to decrease must not be greater than in stock quantity."); }

        this.quantity -= quantity;
        this.version += 1;
    }

    public UUID getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(getId(), inventory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", product=" + product +
                ", version=" + version +
                '}';
    }

    public static class InventoryBuilder {
        private Integer quantity;
        private Product product;

        public InventoryBuilder() {}

        public InventoryBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public InventoryBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public Inventory build() {
            if (quantity <= 0 || product == null) {
                throw new ValidationException("All Inventory core must be provided.");
            }

            return new Inventory(this.quantity, this.product);
        }
    }
}
