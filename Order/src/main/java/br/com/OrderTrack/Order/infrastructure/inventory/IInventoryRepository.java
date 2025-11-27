package br.com.OrderTrack.Order.infrastructure.inventory;

import br.com.OrderTrack.Order.infrastructure.product.ProductEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IInventoryRepository extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByProductName(@NotBlank String productName);

    @Query("SELECT product p FROM InventoryEntity")
    Page<ProductEntity> findAllProducts(Pageable pageable);
}
