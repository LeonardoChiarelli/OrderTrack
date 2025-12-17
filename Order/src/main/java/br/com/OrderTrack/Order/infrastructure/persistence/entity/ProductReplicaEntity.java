package br.com.OrderTrack.Order.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products_replica")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReplicaEntity {
    @Id
    private UUID id;
    private String name;
    private BigDecimal price;
    private boolean active;
}