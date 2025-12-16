package br.com.OrderTrack.Inventory.infrastructure.inventory;

import br.com.OrderTrack.Order.domain.inventory.Inventory;
import br.com.OrderTrack.Order.infrastructure.product.ProductEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "inventories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer quantity;

    @OneToOne
    private ProductEntity productEntity;

    @Version
    private Integer version;

    public InventoryEntity(@NotNull @Positive @Min(1) Integer quantity, ProductEntity productEntity) {
        this.quantity = quantity;
        this.productEntity = productEntity;
    }

    public InventoryEntity(Inventory inventory, ProductEntity productEntity) {
        this.id = inventory.getId();
        this.quantity = inventory.getQuantity();
        this.productEntity = productEntity;
        this.version = inventory.getVersion();
    }

    public void addQuantity(Integer quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(Integer quantity){
        this.quantity -= quantity;
    }
}
