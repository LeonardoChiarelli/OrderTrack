package br.com.OrderTrack.Order.infrastructure.product;

import br.com.OrderTrack.Order.application.product.dto.CreateProductDTO;
import br.com.OrderTrack.Order.domain.product.Product;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private boolean active;

    public ProductEntity(@Valid CreateProductDTO dto) {
        this.name = dto.name();
        this.description = dto.description();
        this.category = dto.category();
        this.price = dto.price();
        this.active = true;
    }

    public ProductEntity(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.active = product.isActive();
    }

    public void changeStatus(boolean status) {
        this.active = status;
    }
}
