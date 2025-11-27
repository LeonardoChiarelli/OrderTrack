package br.com.OrderTrack.Track.domain.model;

import br.com.OrderTrack.Order.domain.dto.CreateProductDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String category;

    private BigDecimal price;
    private boolean active;

    public Product(@Valid CreateProductDTO dto) {
        this.name = dto.name();
        this.description = dto.description();
        this.category = dto.category();
        this.price = dto.price();
        this.active = true;
    }

    public void changeStatus(boolean status) {
        this.active = status;
    }
}
